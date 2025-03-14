package li.cil.ocreloaded.minecraft.common.sound;

import net.minecraft.core.BlockPos;

public interface SoundPlayerProvider {
    
    void playBeepSound(BlockPos position, short frequency, short duration);

}
