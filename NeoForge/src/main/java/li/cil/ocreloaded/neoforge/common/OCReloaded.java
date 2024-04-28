package li.cil.ocreloaded.neoforge.common;


import li.cil.ocreloaded.neoforge.client.OCReloadedClient;
import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import li.cil.ocreloaded.minecraft.server.CommonServerHooks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@Mod(OCReloadedCommon.MOD_ID)
public class OCReloaded {

    public OCReloaded(IEventBus eventBus) {
        NeoForge.EVENT_BUS.register(this);
        CommonRegistered.initialize();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            new OCReloadedClient(eventBus);
        }
    }

    @SubscribeEvent
    public void serverStarted(ServerStartedEvent event) {
        CommonServerHooks.setup(event.getServer());
    }

}