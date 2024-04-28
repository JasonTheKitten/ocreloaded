package li.cil.ocreloaded.forge.client;

import li.cil.ocreloaded.minecraft.client.CommonClientHooks;
import li.cil.ocreloaded.minecraft.client.registry.ClientRegistered;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class OCReloadedClient {

    public OCReloadedClient() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        ClientRegistered.setup();
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        CommonClientHooks.setup();
    }

}
