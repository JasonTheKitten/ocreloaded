package li.cil.ocreloaded.minecraft.common.network;

import commonnetwork.api.Network;
import li.cil.ocreloaded.minecraft.common.network.packets.PowerPacket;
import li.cil.ocreloaded.minecraft.common.network.packets.ScreenPacket;
import li.cil.ocreloaded.minecraft.common.network.packets.SoundPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkUtil {
    public static final Logger LOGGER = LoggerFactory.getLogger(NetworkUtil.class);
    private NetworkUtil() {}

    public static void initialize() {
        Network.registerPacket(PowerPacket.type(), PowerPacket.class, PowerPacket.STREAM_CODEC, PowerPacket::handle);
        Network.registerPacket(ScreenPacket.type(), ScreenPacket.class, ScreenPacket.STREAM_CODEC, ScreenPacket::handle);
        Network.registerPacket(SoundPacket.type(), SoundPacket.class, SoundPacket.STREAM_CODEC, SoundPacket::handle);
    }
}
