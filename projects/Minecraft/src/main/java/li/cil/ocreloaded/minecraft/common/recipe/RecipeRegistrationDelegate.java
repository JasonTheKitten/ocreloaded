package li.cil.ocreloaded.minecraft.common.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;

public interface RecipeRegistrationDelegate {
    
    void registerRecipe(ResourceLocation resource, Recipe<? extends RecipeInput> recipe, RecipeType<?> type);

}
