package li.cil.ocreloaded.forge.common.network;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import li.cil.ocreloaded.minecraft.common.network.ClientNetworkMessageContext;
import li.cil.ocreloaded.minecraft.common.network.NetworkHandler;
import li.cil.ocreloaded.minecraft.common.network.NetworkInterface;
import li.cil.ocreloaded.minecraft.common.network.NetworkMessage;
import li.cil.ocreloaded.minecraft.common.network.ServerNetworkMessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

public class ForgeNetworkInterface implements NetworkInterface {
    
    private static final int PROTOCOL_VERSION = 1;

    private static final SimpleChannel NETWORK_CHANNEL = ChannelBuilder.named(new ResourceLocation("ocreloaded", "main"))
        .clientAcceptedVersions((status, version) -> version == PROTOCOL_VERSION)
        .serverAcceptedVersions((status, version) -> version == PROTOCOL_VERSION)
        .networkProtocolVersion(PROTOCOL_VERSION)
        .simpleChannel();

    private final Map<Integer, NetworkHandler<?>> handlers = new HashMap<>();
    private final Map<String, Integer> handlerIds = new HashMap<>();
    private int nextHandlerId = 0;

    @Override
    public void registerNetworkHandler(NetworkHandler<?> handler) {
        registerHandlerTypes(handler);
    }

    @Override
    public void messageClient(NetworkMessage message, Player recipient) {
        NETWORK_CHANNEL.send(message, PacketDistributor.PLAYER.with((ServerPlayer) recipient));
    }

    @Override
    public void messageServer(NetworkMessage message) {
        NETWORK_CHANNEL.send(message, PacketDistributor.SERVER.noArg());
    }

    @SuppressWarnings("unchecked")
    private <T extends NetworkMessage> void registerHandlerTypes(NetworkHandler<?> handler) {
        NetworkHandler<T> handlerT = (NetworkHandler<T>) handler;
        int handlerId = nextHandlerId++;
        handlers.put(handlerId, handler);
        handlerIds.put(handler.getType(), handlerId);
        BiConsumer<T, FriendlyByteBuf> encoder = (message, buffer) -> handlerT.encode(buffer, message);
        Function<FriendlyByteBuf, T> decoder = buffer -> handlerT.decode(buffer);
        NETWORK_CHANNEL.messageBuilder(handlerT.getMessageType())
            .encoder(encoder)
            .decoder(decoder)
            .consumerMainThread((message, context) -> handleMessage(handlerT, message, context))
            .add();
    }

    @SuppressWarnings("resource")
    private <T extends NetworkMessage> void handleMessage(NetworkHandler<T> handler, T message, CustomPayloadEvent.Context context) {
        NetworkDirection direction = context.getDirection();
        if (direction == NetworkDirection.PLAY_TO_SERVER) {
            ServerNetworkMessageContext serverContext = new ServerNetworkMessageContext(context.getSender());
            handler.handleServer(message, serverContext);
        } else if (direction == NetworkDirection.PLAY_TO_CLIENT) {
            // TODO: Don't rely on client package in common code{
            ClientNetworkMessageContext clientContext = new ClientNetworkMessageContext(Minecraft.getInstance().player);
            handler.handleClient(message, clientContext);
        }
        context.setPacketHandled(true);
    }
    
}
