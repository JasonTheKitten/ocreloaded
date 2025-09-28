package li.cil.ocreloaded.minecraft.common.network;

import net.minecraft.world.entity.player.Player;

public interface NetworkInterface {

    void registerNetworkHandler(NetworkHandler<?> handler);
    
    void messageClient(NetworkMessage message, Player recipient);

    void messageServer(NetworkMessage message);

}
