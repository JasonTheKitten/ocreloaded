package li.cil.ocreloaded.minecraft.client.sound;

import li.cil.ocreloaded.minecraft.common.sound.SoundPlayerProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class SoundPlayer implements SoundPlayerProvider {
    
    @Override
    public void playBeepSound(BlockPos position, short frequency, short duration) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath("ocreloaded", "beep");
        BeepSound beepSound = new BeepSound(resourceLocation, position, frequency, duration);
        Minecraft.getInstance().getSoundManager().play(beepSound);
    }

}
