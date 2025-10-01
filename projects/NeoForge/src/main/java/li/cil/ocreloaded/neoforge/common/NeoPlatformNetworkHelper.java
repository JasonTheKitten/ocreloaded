package li.cil.ocreloaded.neoforge.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.LoggerFactory;

import li.cil.ocreloaded.minecraft.common.network.IPlatformNetworkHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NeoPlatformNetworkHelper implements IPlatformNetworkHelper {

    private final Map<Class<?>, Type<CustomPacketPayload>> typeMap = new HashMap<>();

    private List<DeferredPacketRegistration<?>> deferredPacketRegistrations = new ArrayList<>();

    @Override
    public void sendToClients(Object packet, List<ServerPlayer> players) {
        Type<CustomPacketPayload> type = typeMap.get(packet.getClass());
        LoggerFactory.getLogger(getClass()).info("SENDINg PACKET {}", type);
        CustomDataPacket<?> dataPacket = new CustomDataPacket<>(type, packet);
        for (ServerPlayer player: players) {
            PacketDistributor.sendToPlayer(player, dataPacket);
        }
    }

    @Override
    public void sendToClientsInLevel(Object packet, ServerLevel level) {
        Type<CustomPacketPayload> type = typeMap.get(packet.getClass());
        LoggerFactory.getLogger(getClass()).info("SENDINg PACKET {}", type);
        CustomDataPacket<?> dataPacket = new CustomDataPacket<>(type, packet);
        PacketDistributor.sendToAllPlayers(dataPacket);
    }

    @Override
    public void sendToServer(Object packet) {
        Type<CustomPacketPayload> type = typeMap.get(packet.getClass());
        LoggerFactory.getLogger(getClass()).info("SENDINg PACKET {}", type);
        CustomDataPacket<?> dataPacket = new CustomDataPacket<>(type, packet);
        PacketDistributor.sendToServer(dataPacket);
    }

    @Override
    public <T> void registerPacket(
        Type<CustomPacketPayload> type, Class<? extends T> packetClass,
        StreamCodec<RegistryFriendlyByteBuf, T> streamCodec, Consumer<PacketContext<T>> packetHandler,
        Side side
    ) {
        if (deferredPacketRegistrations == null) {
            throw new IllegalStateException("Cannot register packet listeners after registration ends!");
        }
        
        deferredPacketRegistrations.add(new DeferredPacketRegistration<>(type, packetClass, streamCodec, packetHandler, side));
    }

    @SubscribeEvent
    public void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        
        for (DeferredPacketRegistration<?> registration: deferredPacketRegistrations) {
            registerPayload(registrar, registration);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void registerPayload(PayloadRegistrar registrar, DeferredPacketRegistration<T> registration) {
        Type<CustomPacketPayload> packetType = registration.packetType();

        typeMap.put(registration.packetClass(), packetType);

        // TODO: Probably Wrong
        StreamCodec<FriendlyByteBuf, T> streamCodec = (StreamCodec<FriendlyByteBuf, T>) registration.streamCodec;
        StreamCodec<? super FriendlyByteBuf, CustomPacketPayload> codec = StreamCodec.of((dest, src) -> {
            streamCodec.encode(dest, ((CustomDataPacket<T>) src).data());
        }, src -> {
            return new CustomDataPacket<T>(packetType, streamCodec.decode(src));
        });

        switch (registration.side) {
            case BOTH:
                registrar.<CustomPacketPayload>playBidirectional(registration.packetType, codec,
                    (payload, context) -> handleMessage(payload, context, registration.packetHandler()));
                break;
            case C2S:
                registrar.<CustomPacketPayload>playToServer(registration.packetType, codec,
                    (payload, context) -> handleMessage(payload, context, registration.packetHandler()));
                break;
            case S2C:
                registrar.<CustomPacketPayload>playToClient(registration.packetType, codec,
                    (payload, context) -> handleMessage(payload, context, registration.packetHandler()));
                break;
            default:
                throw new IllegalArgumentException("Not a valid side!");
        }
    }

    <T> void handleMessage(CustomPacketPayload payload, IPayloadContext context, Consumer<PacketContext<T>> packetHandler) {
        context.enqueueWork(() -> {
            packetHandler.accept(new PacketContext<T>() {

                @Override
                @SuppressWarnings("unchecked")
                public T message() {
                    return ((CustomDataPacket<T>) payload).data();
                }

                @Override
                public Side side() {
                    return context.player().level().isClientSide() ? Side.S2C : Side.C2S;
                }

                @Override
                public Player sender() {
                    return context.player();
                }

            });
        });
    };

    private static record DeferredPacketRegistration<T>(
        Type<CustomPacketPayload> packetType, Class<? extends T> packetClass,
        StreamCodec<? extends FriendlyByteBuf, T> streamCodec, Consumer<PacketContext<T>> packetHandler,
        Side side
    ) {}
    
}
