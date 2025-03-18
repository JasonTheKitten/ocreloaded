package li.cil.ocreloaded.minecraft.common.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface RecipeRegistrationDelegate {
    
    void registerRecipe(ResourceLocation resource, Recipe<? extends Container> recipe, RecipeType<?> type);

}
