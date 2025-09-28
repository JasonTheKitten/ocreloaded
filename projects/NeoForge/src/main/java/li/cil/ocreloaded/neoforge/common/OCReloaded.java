package li.cil.ocreloaded.neoforge.common;


import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import li.cil.ocreloaded.minecraft.server.CommonServerHooks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@Mod(OCReloaded.MOD_ID)
@EventBusSubscriber(modid = OCReloaded.MOD_ID)
public class OCReloaded {
    public static final String MOD_ID = "ocreloaded";

    public OCReloaded() {
        CommonRegistered.initialize();
    }

    @SubscribeEvent
    public static void serverStarted(ServerStartedEvent event) {
        CommonServerHooks.setup(event.getServer());
    }
}