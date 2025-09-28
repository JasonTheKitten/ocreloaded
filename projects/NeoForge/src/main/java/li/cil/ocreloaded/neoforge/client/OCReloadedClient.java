package li.cil.ocreloaded.neoforge.client;

import li.cil.ocreloaded.minecraft.client.CommonClientHooks;
import li.cil.ocreloaded.minecraft.client.registry.ClientRegistered;
import li.cil.ocreloaded.neoforge.common.OCReloaded;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = OCReloaded.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = OCReloaded.MOD_ID, value = Dist.CLIENT)
public class OCReloadedClient {
    public OCReloadedClient() {

    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ClientRegistered.setup();
        CommonClientHooks.setup();
    }
}
