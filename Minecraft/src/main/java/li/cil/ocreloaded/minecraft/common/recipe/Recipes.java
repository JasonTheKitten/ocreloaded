package li.cil.ocreloaded.minecraft.common.recipe;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigIncludeContext;
import com.typesafe.config.ConfigIncluder;
import com.typesafe.config.ConfigIncluderFile;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigSyntax;
import com.typesafe.config.ConfigValue;

import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public final class Recipes {

    private static final Logger LOGGER = LoggerFactory.getLogger(Recipes.class);

    private static final Recipes INSTANCE = new Recipes();

    private static final String[] RECIPE_SETS = new String[] {
        "default", "hardmode", "gregtech", "peaceful"
    };

    private final Map<String, RecipeProcessor> recipeHandlers = new HashMap<>();

    private Recipes() {}
    
    public void init(MinecraftServer server, RecipeRegistrationDelegate registrationDelegate) throws IOException {
        File recipeDirectory = new File(server.getServerDirectory() + File.separator + "opencomputers");
        copyDefaultRecipes(recipeDirectory, server);
        Config config = loadConfig(recipeDirectory);
        registerRecipes(config, registrationDelegate);
    }

    public void registerRecipeHandler(String type, RecipeProcessor processor) {
        recipeHandlers.put(type, processor);
    }

    private static void copyDefaultRecipes(File recipeDirectory, MinecraftServer server) throws IOException {
        if (!recipeDirectory.exists()) {
            recipeDirectory.mkdirs();
        }
        for (String recipeSet : RECIPE_SETS) {
            InputStream fileStream = ClassLoader.getSystemResourceAsStream("assets/ocreloaded/recipes/" + recipeSet + ".recipes");
            File recipeFile = new File(recipeDirectory, recipeSet + ".recipes");
            Files.copy(fileStream, recipeFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        File userRecipeFile = new File(recipeDirectory, "user.recipes");
        if (!userRecipeFile.exists()) {
            InputStream fileStream = ClassLoader.getSystemResourceAsStream("assets/ocreloaded/recipes/user.recipes");
            Files.copy(fileStream, userRecipeFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static Config loadConfig(File recipeDirectory) {
        ConfigParseOptions configParseOptions = ConfigParseOptions.defaults()
            .setSyntax(ConfigSyntax.CONF)
            .setIncluder(new MyConfigIncluder(recipeDirectory));

        File recipeFile = new File(recipeDirectory, "user.recipes");
        LoggerFactory.getLogger(Recipes.class).info("Loading recipe file {}", recipeFile.toString());
        return ConfigFactory.parseFile(recipeFile, configParseOptions);
    }

    private void registerRecipes(Config config, RecipeRegistrationDelegate registrationDelegate) {
        for (Item item: BuiltInRegistries.ITEM) {
            ResourceLocation key = item.arch$registryName();
            if (!key.getNamespace().equals(OCReloadedCommon.MOD_ID)) continue;

            String name = key.getPath();
            if (config.hasPath(name) || config.hasPath(toLegacyName(name))) {
                ConfigValue value = config.hasPath(name) ?
                    config.getValue(name) :
                    config.getValue(toLegacyName(name));
                switch (value.valueType()) {
                    case OBJECT:
                        Config innerConfig = config.hasPath(name) ?
                            config.getConfig(name) :
                            config.getConfig(toLegacyName(name));
                        addRecipe(registrationDelegate, new ItemStack(item), innerConfig, name);
                        break;
                    case BOOLEAN:
                        if (!(boolean) value.unwrapped()) {
                            hide(item);
                        }
                        break;
                    default:
                        LOGGER.error("Failed adding recipe for '{}', you will not be able to craft this item. The error was: Invalid value for recipe.", name);
                        break;
                }
            } else {
                LOGGER.warn("No recipe for '{}', you will not be able to craft this item. To suppress this warning, disable the recipe (assign `false` to it).", name);
            }
        }
    }

    private static void hide(Item item) {
        // TODO: Implement
    }

    private void addRecipe(RecipeRegistrationDelegate registrationDelegate, ItemStack output, Config recipe, String name) {
        try {
            String recipeType = tryGetType(recipe);
            RecipeProcessor recipeHandler = recipeHandlers.get(recipeType);
            if (recipeHandler != null) {
                recipeHandler.process(registrationDelegate, output, recipe);
            } else {
                LOGGER.error("Failed adding recipe for " + name + ", you will not be able to craft this item. The error was: Invalid recipe type '" + recipeType + "'.");
            }
        } catch (RecipeException e) {
            LOGGER.error("Failed adding recipe for " + name + ", you will not be able to craft this item.", e);
            LOGGER.error(e.getMessage());
        } catch (RuntimeException e) {
            if (e.getCause() instanceof RecipeException) {
                LOGGER.error("Failed adding recipe for " + name + ", you will not be able to craft this item.");
                LOGGER.error(e.getCause().getMessage());
            } else {
                throw e;
            }
        }
    }
    
    private String tryGetType(Config recipe) {
        return recipe.hasPath("type") ? recipe.getString("type") : "shaped";
    }

    public static int tryGetCount(Config recipe) {
        return recipe.hasPath("count") ? recipe.getInt("count") : 1;
    }

    @SuppressWarnings("unchecked")
    public static Ingredient parseIngredient(Object entry) {
        if (entry == null) {
            return null;
        } else if (entry instanceof Map map) {
            return parseMapIngredient(map);
        } else if (entry instanceof String name) {
            return parseStringIngredient(name);
        } else {
            throw new RuntimeException(new RecipeException("Invalid ingredient type (not a map or string): " + entry));
        }
    }

    private static Ingredient parseMapIngredient(Map<Object, Object> map) {
        // TODO: Lookup ore dictionary
        if (map.containsKey("item")) {
            Object value = map.get("item");
            if (value instanceof String) {
                return parseItemIngredient((String) value);
            } else {
                throw new RuntimeException(new RecipeException("Invalid item name in recipe (not a string: " + value + ")."));
            }
        } else if (map.containsKey("block")) {
            Object value = map.get("block");
            if (value instanceof String) {
                return parseBlockIngredient((String) value);
            } else {
                throw new RuntimeException(new RecipeException("Invalid block name (not a string: " + value + ")."));
            }
        } else {
            throw new RuntimeException(new RecipeException("Invalid ingredient type (no oreDict, item or block entry)."));
        }
    }

    private static Ingredient parseItemIngredient(String name) {
        Item item = findItem(name);
        if (item == null || item.equals(ItemStack.EMPTY.getItem())) {
            item = findItem(toModernName(name));
        }
        if (item == null || item.equals(ItemStack.EMPTY.getItem())) {
            throw new RuntimeException(new RecipeException("No item found with name '" + name + "'."));
        }

        return Ingredient.of(item);
    }

    private static Ingredient parseBlockIngredient(String name) {
        Block block = findBlock(name);
        if (block == null || block.equals(Block.byItem(ItemStack.EMPTY.getItem()))) {
            block = findBlock(toModernName(name));
        }
        if (block == null || block.equals(Block.byItem(ItemStack.EMPTY.getItem()))) {
            throw new RuntimeException(new RecipeException("No block found with name '" + name + "'."));
        }

        return Ingredient.of(block);
    }

    private static Ingredient parseStringIngredient(String name) {
        if (name.trim().isEmpty()) {
            return null;
        }

        // TODO: Lookup ore dictionary

        Item item = findItem(name);
        if (item == null || item.equals(ItemStack.EMPTY.getItem())) {
            item = findItem(toModernName(name));
        }
        if (item == null || item.equals(ItemStack.EMPTY.getItem())) {
            throw new RuntimeException(new RecipeException("No ore dictionary entry, item or block found for ingredient with name '" + name + "'."));
        }

        return Ingredient.of(item);
    }

    private static Item findItem(String name) {
        return BuiltInRegistries.ITEM.get(safeResourceLocation(name));
    }

    private static Block findBlock(String name) {
        return BuiltInRegistries.BLOCK.get(safeResourceLocation(name));
    }

    private static String toLegacyName(String modernName) {
        return switch(modernName) {
            case "memory1" -> "ram1";
            case "memory1_5" -> "ram2";
            case "memory2" -> "ram3";
            case "memory2_5" -> "ram4";
            case "memory3" -> "ram5";
            case "memory3_5" -> "ram6";
            case "harddiskdrive1" -> "hdd1";
            case "harddiskdrive2" -> "hdd2";
            case "harddiskdrive3" -> "hdd3";
            case "eeprom_lua" -> "eeprom"; // We have two EEPROMS, but the old version had them merged
            default -> modernName;
        };
    }

    private static String toModernName(String legacyName) {
        String[] nameParts = legacyName.split(":");
        String provider = nameParts.length > 1 ? nameParts[0] : null;
        String name = nameParts.length > 1 ? nameParts[1] : nameParts[0];

        String newProvider = provider != null && provider.equals("oc") ? OCReloadedCommon.MOD_ID : provider;
        String newName = switch(name) {
            case "ram1" -> "memory1";
            case "ram2" -> "memory1_5";
            case "ram3" -> "memory2";
            case "ram4" -> "memory2_5";
            case "ram5" -> "memory3";
            case "ram6" -> "memory3_5";
            case "hdd1" -> "harddiskdrive1";
            case "hdd2" -> "harddiskdrive2";
            case "hdd3" -> "harddiskdrive3";
            case "eeprom" -> "eeprom_lua"; // We have two EEPROMS, but the old version had them merged
            default -> name;
        };
        newName = newName.replaceAll("([A-Z])", "_$1").toLowerCase();

        return newProvider != null ? newProvider + ":" + newName : newName;
    }

    private static ResourceLocation safeResourceLocation(String name) {
        return new ResourceLocation(name.toLowerCase());
    }

    private static class MyConfigIncluder implements ConfigIncluder, ConfigIncluderFile {

        private final File recipeDirectory;

        private ConfigIncluder fallback;

        public MyConfigIncluder(File recipeDirectory) {
            this.recipeDirectory = recipeDirectory;
        }

        @Override
        public ConfigIncluder withFallback(ConfigIncluder fallback) {
            this.fallback = fallback;
            return this;
        }

        @Override
        public ConfigObject include(ConfigIncludeContext context, String what) {
            return fallback.include(context, what);
        }

        @Override
        public ConfigObject includeFile(ConfigIncludeContext context, File what) {
            try {
                LoggerFactory.getLogger(MyConfigIncluder.class).info("Loading recipe file {}", new File(recipeDirectory, what.getPath()).toString());
                try (FileReader reader = what.isAbsolute() ?
                    new FileReader(what) :
                    new FileReader(new File(recipeDirectory, what.getPath()))
                ) {
                    return ConfigFactory
                        .parseReader(reader, ConfigParseOptions.defaults().setSyntax(ConfigSyntax.CONF).setIncluder(this))
                        .root();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Recipes getInstance() {
        return INSTANCE;
    }
    
}
