package li.cil.ocreloaded.minecraft.common.network;

import java.util.HashMap;
import java.util.Map;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class NetworkUtil {

    public static final NetworkUtil INSTANCE = new NetworkUtil();

    private final Map<String, NetworkHandler<?>> HANDLERS = new HashMap<>();
    
    private NetworkUtil() {}

    @SuppressWarnings("unchecked")
    public void messageClient(NetworkMessage message, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            ResourceLocation location = new ResourceLocation(OCReloadedCommon.MOD_ID, message.getType());
            NetworkHandler<NetworkMessage> handler = (NetworkHandler<NetworkMessage>) HANDLERS.get(location.toString());
            if (handler == null) {
                throw new IllegalArgumentException("No handler for message type: " + location);
            }

            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
            handler.encode(buffer, message);
            
            
            NetworkManager.sendToPlayer(serverPlayer, location, buffer);
        } else {
            throw new IllegalArgumentException("Player is not a server player.");
        }
    }

    @SuppressWarnings("unchecked")
    public void messageServer(NetworkMessage message) {
        ResourceLocation location = new ResourceLocation(OCReloadedCommon.MOD_ID, message.getType());
        NetworkHandler<NetworkMessage> handler = (NetworkHandler<NetworkMessage>) HANDLERS.get(location.toString());
        if (handler == null) {
            throw new IllegalArgumentException("No handler for message type: " + location);
        }

        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        handler.encode(buffer, message);

        NetworkManager.sendToServer(location, buffer);
    }

    @SuppressWarnings("unchecked")
    public void registerHandler(String type, NetworkHandler<?> handler) {
        HANDLERS.put(type, handler);

        ResourceLocation location = new ResourceLocation(OCReloadedCommon.MOD_ID, handler.getType());
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, location, (buffer, context) -> {
            context.queue(() -> {
                NetworkHandler<NetworkMessage> messageHandler = (NetworkHandler<NetworkMessage>) HANDLERS.get(location.toString());
                if (messageHandler == null) {
                    throw new IllegalArgumentException("No handler for message type: " + location);
                }

                NetworkMessage message = messageHandler.decode(buffer);
                messageHandler.handleClient(message, new ClientNetworkMessageContext(context.getPlayer()));
            });
        });

        NetworkManager.registerReceiver(NetworkManager.Side.C2S, location, (buffer, context) -> {
            context.queue(() -> {
                NetworkHandler<NetworkMessage> messageHandler = (NetworkHandler<NetworkMessage>) HANDLERS.get(location.toString());
                if (messageHandler == null) {
                    throw new IllegalArgumentException("No handler for message type: " + location);
                }

                NetworkMessage message = messageHandler.decode(buffer);
                messageHandler.handleServer(message, new ServerNetworkMessageContext(context.getPlayer()));
            });
        });
    }

    public static NetworkUtil getInstance() {
        return INSTANCE;
    }

}
