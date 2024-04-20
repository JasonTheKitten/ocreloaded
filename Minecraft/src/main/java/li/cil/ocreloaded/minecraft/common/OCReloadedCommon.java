package li.cil.ocreloaded.minecraft.common;

import java.util.List;

import li.cil.ocreloaded.minecraft.common.network.NetworkHandler;
import li.cil.ocreloaded.minecraft.common.network.power.PowerNetworkHandler;

public class OCReloadedCommon {

    public static final String MOD_ID = "ocreloaded";

    public static List<NetworkHandler<?>> NETWORK_HANDLERS = List.of(
        new PowerNetworkHandler()
    );

}
