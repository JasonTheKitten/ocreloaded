package li.cil.ocreloaded.minecraft.common.network;

import java.util.List;

import li.cil.ocreloaded.minecraft.common.network.power.PowerNetworkHandler;

public class NetworkHandlers {
    
    public static final List<NetworkHandler<?>> HANDLERS = List.of(
        new PowerNetworkHandler()
    );

}
