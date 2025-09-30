package li.cil.ocreloaded.minecraft.server.machine.lua;



import java.io.File;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import li.cil.ocreloaded.core.machine.architecture.luac.LuaCStateFactory;
import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import li.cil.repack.com.naef.jnlua.LuaState;
import li.cil.repack.com.naef.jnlua.LuaState.Library;
import li.cil.repack.com.naef.jnlua.LuaStateFiveThree;
import li.cil.repack.com.naef.jnlua.NativeSupport;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;

public class LuaCFactory {

    private static final List<Library> LIBRARIES_52 = List.of(
        Library.BASE, Library.BIT32, Library.COROUTINE, Library.DEBUG, Library.ERIS, Library.MATH, Library.STRING, Library.TABLE
    );

    private static final List<Library> LIBRARIES_53 = List.of(
        Library.BASE, Library.COROUTINE, Library.DEBUG, Library.ERIS, Library.MATH, Library.STRING, Library.TABLE
    );

    private static final Logger LOGGER = LoggerFactory.getLogger(LuaCFactory.class);

    private final MinecraftServer server;

    private final String executableName;
    private final String tempDir;
    
    public LuaCFactory(MinecraftServer server) {
        this.executableName = determineExecutableName();
        this.tempDir = System.getProperty("java.io.tmpdir");
        this.server = server;
    }

    public boolean isAvailable(String architecture) {
        Optional<Resource> resource = null;
        boolean isAvailable =
            executableName != null
            && tempDir != null
            && (resource = getResource(architecture)).isPresent();

        if (isAvailable && resource != null) {
            LOGGER.trace("LuaJIT native library available for this platform: " + resource.get());
        } else {
            LOGGER.error("LuaJIT native library not available for this platform.");
        }

        return isAvailable;
    }

    public Optional<LuaCStateFactory> createFactory(String architecture) {
        if (!isAvailable(architecture)) return Optional.empty();
        if (!ensureResourceCopied(architecture)) return Optional.empty();
        LoggerFactory.getLogger(getClass()).info("Loading arch {}", architecture);
        switch (architecture) {
            case "lua52":
                NativeSupport.getInstance().setLoader(() -> System.load(getTempName(architecture)));
                return loaded(() -> new LuaState(), LIBRARIES_52);
            case "lua53":
                // Lua53 still needs Lua52 libs
                if (createFactory("lua52").isEmpty()) return Optional.empty();
                NativeSupport.getInstance().setLoader(() -> {
                    System.load(getTempName("lua52"));
                    System.load(getTempName(architecture));
                });
                return loaded(() -> new LuaStateFiveThree(), LIBRARIES_53);
            default:
                return Optional.empty();
        }
    }

    private Optional<Resource> getResource(String architecture) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(OCReloadedCommon.MOD_ID, "libs/" + architecture + "/" + executableName);
        return server.getResourceManager().getResource(resourceLocation);
    }

    private String getTempName(String architecture) {
        return tempDir + "/OpenComputersReloaded-" + architecture + "-" + executableName;
    }

    private Optional<LuaCStateFactory> loaded(LuaCStateFactory factory, List<Library> libraries) {
        LuaCStateFactory luaCStateFactory = () -> {
            LuaState luaState = factory.create();
            libraries.forEach(luaState::openLib);
            luaState.pop(libraries.size());
            return luaState;
         };

        return Optional.of(luaCStateFactory);
    }

    private String determineExecutableName() {
        try {
            String osArch = System.getProperty("os.arch").toLowerCase();
            String osName = System.getProperty("os.name").toLowerCase();

            boolean is64Bit = osArch.startsWith("amd64") || osArch.startsWith("x86_64");
            boolean is32Bit = osArch.startsWith("x86") || osArch.startsWith("i386");
            boolean isArm = osArch.startsWith("arm");

            if (osName.contains("linux") && isArm) return "native.arm.so";
            if (!is64Bit && !is32Bit) return null;

            if (osName.contains("windows")) {
                return is64Bit ? "native.64.dll" : "native.32.dll";
            } else if (osName.contains("linux")) {
                return is64Bit ? "native.64.so" : "native.32.so";
            } else if (osName.contains("mac")) {
                return is64Bit ? "native.64.dylib" : "native.32.dylib";
            } else if (osName.contains("free")) {
                return is64Bit ? "native.64.bsd.so" : "native.32.bsd.so";
            }
        } catch (Exception e) {}

        return null;
    }

    private boolean ensureResourceCopied(String architecture) {
        String tempName = getTempName(architecture);
        Optional<Resource> resource = getResource(architecture);

        if (resource.isEmpty()) {
            LOGGER.error("Failed to locate LuaJIT native library for this platform.");
            return false;
        }

        if (new File(tempName).exists()) {
            LOGGER.trace("LuaJIT native library already copied to temporary directory.");
            new File(tempName).delete();
            //return true;
        }

        try (InputStream in = resource.get().open();) {
            Files.copy(in, new File(tempName).toPath());
            LOGGER.trace("Copied LuaJIT native library to temporary directory: {}.", tempName);
            return true;
        } catch (FileAlreadyExistsException e) {
            LOGGER.trace("The native libraries already exist, and their handles are locked. This is a windows thing, and nothing won't happen if we won't copy them over once more (probably)");
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to copy LuaJIT native library to temporary directory.", e);
            return false;
        }
    }

}
