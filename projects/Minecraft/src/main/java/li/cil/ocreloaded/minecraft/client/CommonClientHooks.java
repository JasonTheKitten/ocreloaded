package li.cil.ocreloaded.minecraft.client;

import li.cil.ocreloaded.minecraft.client.sound.SoundPlayer;
import li.cil.ocreloaded.minecraft.common.registry.ClientBridge;
import li.cil.ocreloaded.minecraft.common.sound.SoundPlayerProvider;

public final class CommonClientHooks {
 
    private CommonClientHooks() {}

    public static void setup() {
        ClientBridge.getInstance().registerClient(SoundPlayerProvider.class, new SoundPlayer());
    }
}
