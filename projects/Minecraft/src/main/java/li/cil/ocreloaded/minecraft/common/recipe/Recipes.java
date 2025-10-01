package li.cil.ocreloaded.minecraft.common.recipe;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.typesafe.config.ConfigValueType;

import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public final class Recipes {

    private static final Logger LOGGER = LoggerFactory.getLogger(Recipes.class);

    private static final Recipes INSTANCE = new Recipes();

    private static final String[] RECIPE_SETS = new String[] {
        "default", "hardmode", "peaceful"
    };

    private static final Map<String, List<String>> ORE_DICT = new HashMap<>();

    private final Map<String, RecipeProcessor> recipeHandlers = new HashMap<>();

    private boolean didInit = false;

    private Recipes() {}
    
    public void init(MinecraftServer server, RecipeRegistrationDelegate registrationDelegate) throws IOException {
        if (didInit) return;
        didInit = true;
        
        File recipeDirectory = new File(server.getServerDirectory() + File.separator + "opencomputers");
        copyDefaultRecipes(recipeDirectory, server);
        loadLegacyOreDict(recipeDirectory);
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
            InputStream fileStream = streamAsset(server, "custom_recipes/" + recipeSet + ".recipes");
            File recipeFile = new File(recipeDirectory, recipeSet + ".recipes");
            Files.copy(fileStream, recipeFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        File userRecipeFile = new File(recipeDirectory, "user.recipes");
        if (!userRecipeFile.exists()) {
            InputStream fileStream = streamAsset(server, "custom_recipes/user.recipes");
            Files.copy(fileStream, userRecipeFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        File legacyOreDictFile = new File(recipeDirectory, "legacy_ore_dict.json");
        InputStream fileStream = streamAsset(server, "custom_recipes/legacy_ore_dict.json");
        Files.copy(fileStream, legacyOreDictFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    @SuppressWarnings("unchecked")
    private static void loadLegacyOreDict(File recipeDirectory) {
        File customLegacyOreDictFile = new File(recipeDirectory, "custom_ore_dict.json");
        File legacyOreDictFile = customLegacyOreDictFile.exists() ?
            customLegacyOreDictFile :
            new File(recipeDirectory, "legacy_ore_dict.json");
        if (!legacyOreDictFile.exists()) {
            return;
        }

        try {
            Config legacyOreDict = ConfigFactory.parseFile(legacyOreDictFile, ConfigParseOptions.defaults().setSyntax(ConfigSyntax.JSON));
            for (Map.Entry<String, ConfigValue> entry : legacyOreDict.entrySet()) {
                if (entry.getValue().valueType() == ConfigValueType.LIST) {
                    ORE_DICT.put(entry.getKey(), (List<String>) entry.getValue().unwrapped());
                } else {
                    LOGGER.warn("Invalid legacy ore dictionary entry '{}', expected list, got {}.", entry.getKey(), entry.getValue().valueType());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed loading legacy ore dictionary, you will not be able to craft items using it.", e);
        }
    }

    private static Config loadConfig(File recipeDirectory) {
        ConfigParseOptions configParseOptions = ConfigParseOptions.defaults()
            .setSyntax(ConfigSyntax.CONF)
            .setIncluder(new MyConfigIncluder(recipeDirectory));

        File recipeFile = new File(recipeDirectory, "user.recipes");
        return ConfigFactory.parseFile(recipeFile, configParseOptions);
    }

    private void registerRecipes(Config config, RecipeRegistrationDelegate registrationDelegate) {
        for (Item item: BuiltInRegistries.ITEM) {
            // TODO: COMEBACK
            /*ResourceLocation key = item.arch$registryName();
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
            }*/
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
        return recipe.hasPath("output") ? recipe.getInt("output") : 1;
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
        if (map.containsKey("oreDict")) {
            Object value = map.get("oreDict");
            if (value instanceof String name) {
                return parseOreDictIngredient(name);
            } else {
                throw new RuntimeException(new RecipeException("Invalid name in recipe (not a string: " + value + ")."));
            }
        } else if (map.containsKey("item")) {
            Object value = map.get("item");
            if (value instanceof String name) {
                return parseItemIngredient(name);
            } else {
                throw new RuntimeException(new RecipeException("Invalid item name in recipe (not a string: " + value + ")."));
            }
        } else if (map.containsKey("block")) {
            Object value = map.get("block");
            if (value instanceof String name) {
                return parseBlockIngredient(name);
            } else {
                throw new RuntimeException(new RecipeException("Invalid block name (not a string: " + value + ")."));
            }
        } else {
            throw new RuntimeException(new RecipeException("Invalid ingredient type (no oreDict, item or block entry)."));
        }
    }

    private static Ingredient parseOreDictIngredient(String name) {
        Optional<Ingredient> ore = getOre(name);
        if (ore.isPresent()) {
            return ore.get();
        }

        throw new RuntimeException(new RecipeException("No ore dictionary entry found for ingredient with name '" + name + "'."));
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

        Optional<Ingredient> ore = getOre(name);
        if (ore.isPresent()) {
            return ore.get();
        }

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
        return BuiltInRegistries.ITEM.get(ResourceLocation.parse(name));
    }

    private static Block findBlock(String name) {
        return BuiltInRegistries.BLOCK.get(ResourceLocation.parse(name));
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
            case "chip_diamond" -> "chipdiamond";
            case "eeprom_lua" -> "luabios";
            default -> modernName.replaceAll("_", "");
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
            case "chipdiamond" -> "chip_diamond";
            case "luabios" -> "eeprom_lua";

            default -> name;
        };
        newName = newName.replaceAll("([A-Z])", "_$1").toLowerCase();

        return newProvider != null ? newProvider + ":" + newName : newName;
    }

    private static Optional<Ingredient> getOre(String name) {
        // TODO: I don't think this actually works...
        Optional<TagKey<Item>> key = switch(name) {
            case "ingotIron" -> Optional.of(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "ingots/iron")));
            case "ingotGold" -> Optional.of(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "ingots/gold")));
            case "gemDiamond" -> Optional.of(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "ores/diamond")));
            case "gemLapis" -> Optional.of(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "ores/lapis")));
            case "emerald" -> Optional.of(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "ores/emerald")));
            case "redstone" -> Optional.of(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "ores/redstone")));
            case "nuggetGold" -> Optional.of(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "nuggets/gold")));
            case "chipDiamond" -> Optional.of(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(OCReloadedCommon.MOD_ID, "chip_diamond")));
            default -> Optional.empty();
        };

        if (key.isEmpty() && name.contains(":")) {
            String adjustedName = name.replaceAll("([A-Z]+)", "_$1").toLowerCase();
            if (adjustedName.startsWith("oc:")) {
                key = Optional.of(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(OCReloadedCommon.MOD_ID, adjustedName.substring(3))));
            } else {
                key = Optional.of(TagKey.create(Registries.ITEM, ResourceLocation.parse(adjustedName)));
            }
        }

        if (key.isPresent()) {
            Ingredient ingredient = Ingredient.of(key.get());
            if (ingredient.getItems().length > 0) {
                return Optional.of(ingredient);
            }
        }

        List<Item> items = ORE_DICT.getOrDefault(name, List.of()).stream()
            .filter(oreName -> !oreName.isEmpty())
            .map(Recipes::findItem)
            .filter(item -> !item.equals(ItemStack.EMPTY.getItem()))
            .toList();
        if (!items.isEmpty()) {
            return Optional.of(Ingredient.of(items.toArray(Item[]::new)));
        }
        
        return Optional.empty();
    }

    private static InputStream streamAsset(MinecraftServer server, String asset) throws IOException {
        return server.getResourceManager().getResource(ResourceLocation.fromNamespaceAndPath(OCReloadedCommon.MOD_ID, asset)).get().open();
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
