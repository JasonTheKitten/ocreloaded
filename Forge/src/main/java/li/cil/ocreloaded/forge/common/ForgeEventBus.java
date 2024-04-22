package li.cil.ocreloaded.forge.common;

import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.server.CommonServerHooks;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = OCReloadedCommon.MOD_ID, bus = Bus.FORGE)
public class ForgeEventBus {
    
    @SubscribeEvent
    public void onServerStart(ServerStartedEvent event) {
        CommonServerHooks.setup(event.getServer());
    }

}
