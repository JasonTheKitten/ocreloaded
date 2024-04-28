package li.cil.ocreloaded.forge.common;


import dev.architectury.platform.forge.EventBuses;
import li.cil.ocreloaded.forge.client.OCReloadedClient;
import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(OCReloadedCommon.MOD_ID)
public class OCReloaded {

    public OCReloaded() {
        MinecraftForge.EVENT_BUS.register(new ForgeEventBus());
        EventBuses.registerModEventBus(OCReloadedCommon.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> OCReloadedClient::new);

        CommonRegistered.initialize();
    }

}