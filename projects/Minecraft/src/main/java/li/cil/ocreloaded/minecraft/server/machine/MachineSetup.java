package li.cil.ocreloaded.minecraft.server.machine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import com.google.common.base.Supplier;

import li.cil.ocreloaded.core.machine.MachineCodeRegistry;
import li.cil.ocreloaded.core.machine.MachineRegistry;
import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.server.machine.fssup.FileSystemSupplierRegistry;
import li.cil.ocreloaded.minecraft.server.machine.fssup.LocalFileSystemSupplier;
import li.cil.ocreloaded.minecraft.server.machine.fssup.LootFileSystemSupplier;
import li.cil.ocreloaded.minecraft.server.machine.lua.LuaMachineRegistryEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

public class MachineSetup {

    public static void setup(MinecraftServer server) {
        registerArchitectures(server);
        registerStartCode(server);
        registerFilesystemSuppliers(server);
    }

    private static void registerArchitectures(MinecraftServer server) {
        MachineRegistry registry = MachineRegistry.getDefaultInstance();
        registry.register("Lua 5.2", new LuaMachineRegistryEntry(server, "lua52"));
        registry.register("Lua 5.3", new LuaMachineRegistryEntry(server, "lua53"));
    }

    private static void registerStartCode(MinecraftServer server) {
        registerLuaStartCode(server);
        registerLuaBiosCode(server);
    }

    private static void registerLuaStartCode(MinecraftServer server) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(OCReloadedCommon.MOD_ID, "lua/machine.lua");
        Supplier<Optional<InputStream>> supplier = createCodeSupplier(server, resourceLocation);
        MachineCodeRegistry.getDefaultInstance().registerMachineCode("Lua 5.2", supplier);
        MachineCodeRegistry.getDefaultInstance().registerMachineCode("Lua 5.3", supplier);
    }

    private static void registerLuaBiosCode(MinecraftServer server) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(OCReloadedCommon.MOD_ID, "lua/bios.lua");
        Supplier<Optional<InputStream>> supplier = createCodeSupplier(server, resourceLocation);
        MachineCodeRegistry.getDefaultInstance().registerBiosCode("lua", supplier);
    }

    private static void registerFilesystemSuppliers(MinecraftServer server) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(OCReloadedCommon.MOD_ID, "loot");
        FileSystemSupplierRegistry.getDefaultInstance().register("loot", (uuid, data) -> LootFileSystemSupplier.createLootFS(server, resourceLocation, data));
        FileSystemSupplierRegistry.getDefaultInstance().register("localfs", (uuid, data) -> LocalFileSystemSupplier.createLocalFS(server, uuid));
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
