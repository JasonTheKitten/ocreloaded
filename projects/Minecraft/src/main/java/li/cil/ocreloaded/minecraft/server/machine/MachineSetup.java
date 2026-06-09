package li.cil.ocreloaded.minecraft.server.machine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.Supplier;

import li.cil.ocreloaded.core.machine.MachineBuilder;
import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import li.cil.ocreloaded.core.machine.filesystem.FileSystemSupplierRegistry;
import li.cil.ocreloaded.minecraft.server.machine.fssup.LocalFileSystemSupplier;
import li.cil.ocreloaded.minecraft.server.machine.fssup.LootFileSystemSupplier;
import li.cil.ocreloaded.minecraft.server.machine.lua.LuaMachineArchitecture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

public class MachineSetup {

    public static void setup(MinecraftServer server) {
        registerArchitectures(server);
        registerFilesystemSuppliers(server);
    }

    private static void registerArchitectures(MinecraftServer server) {
        MachineBuilder builder = MachineBuilder.getDefaultInstance();
        Supplier<Optional<InputStream>> luaStartCode = createCodeSupplier(
            server,
            ResourceLocation.fromNamespaceAndPath(OCReloadedCommon.MOD_ID, "lua/machine.lua"));

        LuaMachineArchitecture lua52 = new LuaMachineArchitecture(server, "lua52");
        builder.registerArchitecture("Lua 5.2", luaStartCode, lua52::createMachine, lua52::isSupported);

        LuaMachineArchitecture lua53 = new LuaMachineArchitecture(server, "lua53");
        builder.registerArchitecture("Lua 5.3", luaStartCode, lua53::createMachine, lua53::isSupported);
    }

    private static void registerFilesystemSuppliers(MinecraftServer server) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(OCReloadedCommon.MOD_ID, "loot");
        FileSystemSupplierRegistry.getDefaultInstance().register("loot", (uuid, data) -> LootFileSystemSupplier.createLootFS(server, resourceLocation, data));
        FileSystemSupplierRegistry.getDefaultInstance().register("localfs", (uuid, data) -> LocalFileSystemSupplier.createLocalFS(server, uuid));
    }

    private static Supplier<Optional<InputStream>> createCodeSupplier(MinecraftServer server, ResourceLocation resourceLocation) {
        return () -> server.getResourceManager().getResource(resourceLocation)
            .flatMap(resource -> {
                try {
                    return Optional.of(resource.open());
                } catch (IOException e) {
                    return Optional.empty();
                }
            });
    }
}
