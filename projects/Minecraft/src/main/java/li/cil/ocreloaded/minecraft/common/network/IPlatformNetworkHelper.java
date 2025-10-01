package li.cil.ocreloaded.minecraft.common.network;

import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public interface IPlatformNetworkHelper {
    
    public static IPlatformNetworkHelper INSTANCE = ServiceLoader.load(IPlatformNetworkHelper.class).findFirst().orElseThrow();

    <T> void registerPacket(
        Type<CustomPacketPayload> packetType, Class<? extends T> packetClass, 
        StreamCodec<RegistryFriendlyByteBuf, T> streamCodec, Consumer<PacketContext<T>> packetHandler,
        Side side
    );

    void sendToClients(Object packet, List<ServerPlayer> players);

    void sendToClientsInLevel(Object packet, ServerLevel level);

    void sendToServer(Object packet);


    interface PacketContext<T> {

        T message();

        Side side();

        Player sender();

    }

    enum Side {
        C2S, S2C, BOTH;
    }


}
