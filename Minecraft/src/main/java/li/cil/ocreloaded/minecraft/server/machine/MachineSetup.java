package li.cil.ocreloaded.minecraft.server.machine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import com.google.common.base.Supplier;

import li.cil.ocreloaded.core.machine.MachineRegistry;
import li.cil.ocreloaded.core.machine.MachineCodeRegistry;
import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.server.machine.lua.LuaMachineRegistryEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

public class MachineSetup {

    public static void setup(MinecraftServer server) {
        registerArchitectures(server);
        registerStartCode(server);
    }

    private static void registerArchitectures(MinecraftServer server) {
        MachineRegistry.getDefaultInstance().register("lua52", new LuaMachineRegistryEntry(server, "lua52"));
    }

    private static void registerStartCode(MinecraftServer server) {
        registerLuaStartCode(server);
        registerLuaBiosCode(server);
    }

    private static void registerLuaStartCode(MinecraftServer server) {
        ResourceLocation resourceLocation = new ResourceLocation(OCReloadedCommon.MOD_ID, "lua/machine.lua");
        Supplier<Optional<InputStream>> supplier = createCodeSupplier(server, resourceLocation);
        MachineCodeRegistry.getDefaultInstance().registerMachineCode("lua52", supplier);
    }

    private static void registerLuaBiosCode(MinecraftServer server) {
        ResourceLocation resourceLocation = new ResourceLocation(OCReloadedCommon.MOD_ID, "lua/bios.lua");
        Supplier<Optional<InputStream>> supplier = createCodeSupplier(server, resourceLocation);
        MachineCodeRegistry.getDefaultInstance().registerBiosCode("lua", supplier);
    }

    private static Supplier<Optional<InputStream>> createCodeSupplier(MinecraftServer server, ResourceLocation resourceLocation) {
        return () -> {
            return server.getResourceManager().getResource(resourceLocation)
                .flatMap(resource -> {
                    try {
                        return Optional.of(resource.open());
                    } catch (IOException e) {
                        return Optional.empty();
                    }
                });
        };
    }
    
}
