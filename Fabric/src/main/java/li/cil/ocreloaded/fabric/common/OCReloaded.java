package li.cil.ocreloaded.fabric.common;

import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import li.cil.ocreloaded.minecraft.server.CommonServerHooks;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

public class OCReloaded implements ModInitializer {

    @Override
    public void onInitialize() {
        runServerHandlers();
        CommonRegistered.initialize();
    }

    private void runServerHandlers() {
        ServerWorldEvents.LOAD.register((server, level) -> {
            CommonServerHooks.setup(server);
        });
    }

}
