package li.cil.ocreloaded.minecraft.server;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;

import li.cil.ocreloaded.minecraft.common.integration.vanilla.VanillaRecipes;
import li.cil.ocreloaded.minecraft.common.recipe.Recipes;
import li.cil.ocreloaded.minecraft.server.machine.MachineSetup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

public final class CommonServerHooks {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonServerHooks.class);
    
    private CommonServerHooks() {}

    public static void setup(MinecraftServer server) {
        MachineSetup.setup(server);
        VanillaRecipes.init();
        registerDynamicRecipes(server);
    }

    private static void registerDynamicRecipes(MinecraftServer server) {
        Map<RecipeType<?>, Map<ResourceLocation, RecipeHolder<?>>> recipes = new HashMap<>();
        try {
            Recipes.getInstance().init(server, (resource, recipe, type) -> {
                Map<ResourceLocation, RecipeHolder<?>> typeRecipes = recipes.computeIfAbsent(type, t -> new HashMap<>());
                typeRecipes.put(resource, new RecipeHolder<>(resource, recipe));
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Hacky way to load our recipes
        RecipeManager recipeManager = server.getRecipeManager();
        try {
            Field recipesField = getRecipesField();
            recipesField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<RecipeType<?>, Map<ResourceLocation, RecipeHolder<?>>> serverRecipes = (Map<RecipeType<?>, Map<ResourceLocation, RecipeHolder<?>>>) recipesField.get(recipeManager);
            Map<RecipeType<?>, Map<ResourceLocation, RecipeHolder<?>>> mergedRecipes = new HashMap<>(serverRecipes);
            recipes.forEach((type, typeRecipes) -> {
                Map<ResourceLocation, RecipeHolder<?>> newTypeRecipes = new HashMap<>(typeRecipes);
                mergedRecipes.put(type, newTypeRecipes);
            });
            Map<RecipeType<?>, Map<ResourceLocation, RecipeHolder<?>>> finalRecipes =
                mergedRecipes.entrySet().stream()
                    .collect(ImmutableMap.toImmutableMap(
                        Map.Entry::getKey, 
                        entry -> ImmutableMap.copyOf(entry.getValue())
                    ));
            recipesField.set(recipeManager, finalRecipes);
        } catch (Exception e) {
            LOGGER.error("Failed to inject recipes into server", e);
        }
    }

    // I don't feel like transformers today, so quick hack until later
    private static Field getRecipesField() {
        for (Field field : RecipeManager.class.getDeclaredFields()) {
            if (field.getName().equals("recipes")) {
                return field;
            }
        }

        // Otherwise, hopefully guess it
        for (Field field : RecipeManager.class.getDeclaredFields()) {
            if (field.getType().equals(Map.class)) {
                return field;
            }
        }

        throw new RuntimeException("Failed to find recipes field in RecipeManager");
    }

}
