package li.cil.ocreloaded;


import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("ocreloaded")
public class OCReloaded {
    public OCReloaded() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }
}
