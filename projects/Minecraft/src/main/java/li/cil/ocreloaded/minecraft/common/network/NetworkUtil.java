package li.cil.ocreloaded.minecraft.common.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import li.cil.ocreloaded.minecraft.common.network.IPlatformNetworkHelper.Side;
import li.cil.ocreloaded.minecraft.common.network.packets.PowerPacket;
import li.cil.ocreloaded.minecraft.common.network.packets.ScreenPacket;
import li.cil.ocreloaded.minecraft.common.network.packets.SoundPacket;

public class NetworkUtil {

    public static final Logger LOGGER = LoggerFactory.getLogger(NetworkUtil.class);

    private NetworkUtil() {}

    public static void initialize() {
        IPlatformNetworkHelper.INSTANCE.registerPacket(PowerPacket.type(), PowerPacket.class, PowerPacket.STREAM_CODEC, PowerPacket::handle, Side.BOTH);
        IPlatformNetworkHelper.INSTANCE.registerPacket(ScreenPacket.type(), ScreenPacket.class, ScreenPacket.STREAM_CODEC, ScreenPacket::handle, Side.BOTH);
        IPlatformNetworkHelper.INSTANCE.registerPacket(SoundPacket.type(), SoundPacket.class, SoundPacket.STREAM_CODEC, SoundPacket::handle, Side.S2C);
    }

}
