package li.cil.ocreloaded.minecraft.common.recipe;

import com.typesafe.config.Config;

import net.minecraft.world.item.ItemStack;

public interface RecipeProcessor {

    void process(RecipeRegistrationDelegate registrationDelegate, ItemStack itemStack, Config recipe) throws RecipeException;

}
