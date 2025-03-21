package li.cil.ocreloaded.fabric.client;

import li.cil.ocreloaded.minecraft.client.CommonClientHooks;
import li.cil.ocreloaded.minecraft.client.registry.ClientRegistered;
import net.fabricmc.api.ClientModInitializer;

// For some reason split source is no longer working, so I moved this to main
public class OCReloadedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientRegistered.setup();
        CommonClientHooks.setup();
    }

}
