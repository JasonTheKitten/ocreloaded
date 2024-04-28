package li.cil.ocreloaded.neoforge.client;

import li.cil.ocreloaded.minecraft.client.CommonClientHooks;
import li.cil.ocreloaded.minecraft.client.registry.ClientRegistered;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class OCReloadedClient {

    public OCReloadedClient(IEventBus eventBus) {
        eventBus.register(this);
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        ClientRegistered.setup();
        CommonClientHooks.setup();
    }

}
