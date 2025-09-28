package li.cil.ocreloaded.minecraft.common.integration.vanilla;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.typesafe.config.Config;

import li.cil.ocreloaded.minecraft.common.recipe.RecipeRegistrationDelegate;
import li.cil.ocreloaded.minecraft.common.recipe.Recipes;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.SmeltingRecipe;

public final class VanillaRecipes {
    
    private VanillaRecipes() {}

    public static void init() {
        Recipes recipes = Recipes.getInstance();
        recipes.registerRecipeHandler("shaped", VanillaRecipes::addShapedRecipe);
        recipes.registerRecipeHandler("shapeless", VanillaRecipes::addShapelessRecipe);
        recipes.registerRecipeHandler("furnace", VanillaRecipes::addFurnaceRecipe);
    }

    private static void addShapedRecipe(RecipeRegistrationDelegate registrationDelegate, ItemStack itemStack, Config recipe) {
        Ingredient[][] ingredients = recipe.getList("input").unwrapped().stream()
            .map(list -> ((List<?>) list).stream()
                .map(Recipes::parseIngredient)
                .toArray(Ingredient[]::new))
            .toArray(Ingredient[][]::new);
        itemStack.setCount(Recipes.tryGetCount(recipe));

        int numIngredients = -1;
        List<String> shape = new ArrayList<>();
        Map<Character, Ingredient> input = new HashMap<>();
        
        for (Ingredient[] row : ingredients) {
            StringBuilder pattern = new StringBuilder();
            
            for (Ingredient ingredient : row) {
                if (ingredient == null) {
                    pattern.append(' ');
                } else {
                    char symbol = (char) ('a' + numIngredients);
                    numIngredients++;
                    pattern.append(symbol);
                    input.put(symbol, ingredient);
                }
            }
            
            shape.add(pattern.toString());
        }

        ShapedRecipePattern shapedRecipePattern = ShapedRecipePattern.of(input, shape);
        ShapedRecipe shapedRecipe = new ShapedRecipe("", CraftingBookCategory.EQUIPMENT, shapedRecipePattern, itemStack);
        ResourceLocation resourceLocation = itemStack.getItem().arch$registryName();

        // TODO: What is the correct group
        registrationDelegate.registerRecipe(resourceLocation, shapedRecipe, RecipeType.CRAFTING);
    }


    private static void addShapelessRecipe(RecipeRegistrationDelegate registrationDelegate, ItemStack itemStack, Config recipe) {
        NonNullList<Ingredient> ingredients = recipe.getList("input").unwrapped().stream()
            .map(Recipes::parseIngredient)
            .collect(Collectors.toCollection(NonNullList::create));
        itemStack.setCount(Recipes.tryGetCount(recipe));

        ShapelessRecipe shapelessRecipe = new ShapelessRecipe("", CraftingBookCategory.EQUIPMENT, itemStack, ingredients);
        ResourceLocation resourceLocation = itemStack.getItem().arch$registryName();
        registrationDelegate.registerRecipe(resourceLocation, shapelessRecipe, RecipeType.CRAFTING);
    }

    private static void addFurnaceRecipe(RecipeRegistrationDelegate registrationDelegate, ItemStack itemStack, Config recipe) {
        Ingredient input = Recipes.parseIngredient(recipe.getValue("input").unwrapped());
        itemStack.setCount(Recipes.tryGetCount(recipe));
        
        SmeltingRecipe smeltingRecipe = new SmeltingRecipe("", CookingBookCategory.MISC, input, itemStack, 0.0f, 200);
        ResourceLocation resourceLocation = itemStack.getItem().arch$registryName();
        registrationDelegate.registerRecipe(resourceLocation, smeltingRecipe, RecipeType.SMELTING);
    }

}
