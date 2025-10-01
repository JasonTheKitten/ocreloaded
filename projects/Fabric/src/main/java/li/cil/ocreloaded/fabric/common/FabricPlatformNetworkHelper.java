package li.cil.ocreloaded.fabric.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.LoggerFactory;

import li.cil.ocreloaded.minecraft.common.network.IPlatformNetworkHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class FabricPlatformNetworkHelper implements IPlatformNetworkHelper {

    private final Map<Class<?>, Type<CustomPacketPayload>> typeMap = new HashMap<>();

    @Override
    public void sendToClients(Object packet, List<ServerPlayer> players) {
        Type<CustomPacketPayload> type = typeMap.get(packet.getClass());
        LoggerFactory.getLogger(getClass()).info("SENDINg PACKET {}", type);
        CustomDataPacket<?> dataPacket = new CustomDataPacket<>(type, packet);
        for (ServerPlayer player: players) {
            ServerPlayNetworking.send(player, dataPacket);
        }
    }

    @Override
    public void sendToClientsInLevel(Object packet, ServerLevel level) {
        sendToClients(packet, level.players());
    }

    @Override
    public void sendToServer(Object packet) {
        Type<CustomPacketPayload> type = typeMap.get(packet.getClass());
        LoggerFactory.getLogger(getClass()).info("SENDINg PACKET {}", type);
        CustomDataPacket<?> dataPacket = new CustomDataPacket<>(type, packet);
        ClientPlayNetworking.send(dataPacket);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void registerPacket(
        Type<CustomPacketPayload> packetType, Class<? extends T> packetClass,
        StreamCodec<RegistryFriendlyByteBuf, T> streamCodec, Consumer<PacketContext<T>> packetHandler,
        Side side
    ) {
        typeMap.put(packetClass, packetType);

        StreamCodec<? super RegistryFriendlyByteBuf, CustomPacketPayload> codec = StreamCodec.of((dest, src) -> {
            streamCodec.encode(dest, ((CustomDataPacket<T>) src).data());
        }, src -> {
            return new CustomDataPacket<T>(packetType, streamCodec.decode(src));
        });

        if (side == Side.S2C || side == Side.BOTH) PayloadTypeRegistry.playS2C().register(packetType, codec);
        if (side == Side.C2S || side == Side.BOTH) PayloadTypeRegistry.playC2S().register(packetType, codec);

        if (side == Side.C2S || side == Side.BOTH) registerServerReceiver(packetHandler, packetType);
        if (side == Side.S2C || side == Side.BOTH) registerClientReceiver(packetHandler, packetType);
    }

    private <T> void registerServerReceiver(Consumer<PacketContext<T>> packetHandler, Type<CustomPacketPayload> packetType) {
        ServerPlayNetworking.registerGlobalReceiver(packetType, (payload, context) -> {
            packetHandler.accept(new PacketContext<T>() {

                @Override
                @SuppressWarnings("unchecked")
                public T message() {
                    return ((CustomDataPacket<T>) payload).data();
                }

                @Override
                public Side side() {
                    return Side.C2S;
                }

                @Override
                public Player sender() {
                    return context.player();
                }

            });
        });
    }

    private <T> void registerClientReceiver(Consumer<PacketContext<T>> packetHandler, Type<CustomPacketPayload> packetType) {
        ClientPlayNetworking.registerGlobalReceiver(packetType, (payload, context) -> {
            packetHandler.accept(new PacketContext<T>() {

                @Override
                @SuppressWarnings("unchecked")
                public T message() {
                    return ((CustomDataPacket<T>) payload).data();
                }

                @Override
                public Side side() {
                    return Side.S2C;
                }

                @Override
                public Player sender() {
                    return context.player();
                }

            });
        });
    }
    
}
