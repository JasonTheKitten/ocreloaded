package li.cil.ocreloaded.fabric.client.fabric;

import java.util.HashMap;
import java.util.Map;

import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.common.network.ClientNetworkMessageContext;
import li.cil.ocreloaded.minecraft.common.network.NetworkHandler;
import li.cil.ocreloaded.minecraft.common.network.NetworkInterface;
import li.cil.ocreloaded.minecraft.common.network.NetworkMessage;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class FabricClientNetworkInterface implements NetworkInterface {

    private final Map<String, NetworkHandler<?>> handlers = new HashMap<>();

    @Override
    public void registerNetworkHandler(NetworkHandler<?> handler) {
        handlers.put(handler.getType(), handler);
        ClientPlayNetworking.registerGlobalReceiver(new ResourceLocation(OCReloadedCommon.MOD_ID, handler.getType()), (client, handler1, buf, responseSender) -> {
            client.execute(() -> {
                NetworkMessage message = handler.decode(buf);
                ClientNetworkMessageContext context = new ClientNetworkMessageContext(client.player);
                handleClientMessage(handler, message, context);
            });
        });
    }

    @Override
    public void messageClient(NetworkMessage message, Player recipient) {
        throw new UnsupportedOperationException("Cannot send messages to client from client");
    }

    @Override
    public void messageServer(NetworkMessage message) {
        ResourceLocation channel = new ResourceLocation(OCReloadedCommon.MOD_ID, message.getType());
        NetworkHandler<?> handler = handlers.get(message.getType());
        if (handler == null) {
            throw new IllegalArgumentException("No handler registered for message type " + message.getType());
        }
        ClientPlayNetworking.send(channel, encodeMessage(handler, message));
    }

    @SuppressWarnings("unchecked")
    private <T extends NetworkMessage> void handleClientMessage(NetworkHandler<T> handler, NetworkMessage message, ClientNetworkMessageContext context) {
        handler.handleClient((T) message, context);
    }

    @SuppressWarnings("unchecked")
    private <T extends NetworkMessage> FriendlyByteBuf encodeMessage(NetworkHandler<T> handler, NetworkMessage message) {
        FriendlyByteBuf buffer = PacketByteBufs.create();
        handler.encode(buffer, (T) message);

        return buffer;
    }
    
}
