package li.cil.ocreloaded.minecraft.server;

import li.cil.ocreloaded.minecraft.server.machine.MachineSetup;
import net.minecraft.server.MinecraftServer;

public final class CommonServerHooks {
    
    private CommonServerHooks() {}

    public static void setup(MinecraftServer server) {
        MachineSetup.setup(server);
    }

}
