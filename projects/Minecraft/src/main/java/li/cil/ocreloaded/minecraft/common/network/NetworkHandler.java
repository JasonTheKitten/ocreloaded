package li.cil.ocreloaded.minecraft.common.network;

import net.minecraft.network.FriendlyByteBuf;

public interface NetworkHandler<T extends NetworkMessage> {

    String getType();

    Class<T> getMessageType();
    
    void encode(FriendlyByteBuf buffer, T message);

    T decode(FriendlyByteBuf buffer);

    default void handleClient(T message, ClientNetworkMessageContext context) {}

    default void handleServer(T message, ServerNetworkMessageContext context) {}

}
