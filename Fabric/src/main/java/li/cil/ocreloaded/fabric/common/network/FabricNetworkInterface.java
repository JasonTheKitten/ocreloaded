package li.cil.ocreloaded.fabric.common.network;

import java.util.HashMap;
import java.util.Map;

import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.common.network.NetworkHandler;
import li.cil.ocreloaded.minecraft.common.network.NetworkInterface;
import li.cil.ocreloaded.minecraft.common.network.NetworkMessage;
import li.cil.ocreloaded.minecraft.common.network.ServerNetworkMessageContext;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class FabricNetworkInterface implements NetworkInterface {
    
    private final Map<String, NetworkHandler<?>> handlers = new HashMap<>();

    private NetworkInterface clientNetworkInterface;

    @Override
    public void registerNetworkHandler(NetworkHandler<?> handler) {
        handlers.put(handler.getType(), handler);
        ServerPlayNetworking.registerGlobalReceiver(new ResourceLocation(OCReloadedCommon.MOD_ID, handler.getType()), (server, player, handler1, buf, responseSender) -> {
            server.execute(() -> {
                NetworkMessage message = handler.decode(buf);
                ServerNetworkMessageContext context = new ServerNetworkMessageContext(player);
                handleServerMessage(handler, message, context);
            });
        });
    }

    @Override
    public void messageClient(NetworkMessage message, Player recipient) {
        ResourceLocation channel = new ResourceLocation(OCReloadedCommon.MOD_ID, message.getType());
        NetworkHandler<?> handler = handlers.get(message.getType());
        if (handler == null) {
            throw new IllegalArgumentException("No handler registered for message type " + message.getType());
        }
        ServerPlayNetworking.send((ServerPlayer) recipient, channel, encodeMessage(handler, message));
    }

    @Override
    public void messageServer(NetworkMessage message) {
        if (clientNetworkInterface != null) {
            clientNetworkInterface.messageServer(message);
            return;
        }
        throw new UnsupportedOperationException("Cannot send messages to server from server");
    }

    public void setClientInterface(NetworkInterface clientNetworkInterface) {
        this.clientNetworkInterface = clientNetworkInterface;
    }

    @SuppressWarnings("unchecked")
    private <T extends NetworkMessage> void handleServerMessage(NetworkHandler<T> handler, NetworkMessage message, ServerNetworkMessageContext context) {
        handler.handleServer((T) message, context);
    }

    @SuppressWarnings("unchecked")
    private <T extends NetworkMessage> FriendlyByteBuf encodeMessage(NetworkHandler<T> handler, NetworkMessage message) {
        FriendlyByteBuf buffer = PacketByteBufs.create();
        handler.encode(buffer, (T) message);

        return buffer;
    }

}
