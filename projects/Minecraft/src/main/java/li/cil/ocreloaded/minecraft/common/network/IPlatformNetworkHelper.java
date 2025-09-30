package li.cil.ocreloaded.minecraft.common.network;

import java.util.List;
import java.util.ServiceLoader;
import java.util.function.BiConsumer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public interface IPlatformNetworkHelper {

    enum Side {
        S2C, C2S;
    }
    
    IPlatformNetworkHelper INSTANCE = ServiceLoader.load(IPlatformNetworkHelper.class).findFirst().orElseThrow();

    void sendToPlayer(ServerPlayer serverPlayer, ResourceLocation location, FriendlyByteBuf buffer);

    void sendToServer(ResourceLocation location, FriendlyByteBuf buffer);

    void sendToPlayers(List<ServerPlayer> players, ResourceLocation location, FriendlyByteBuf buffer);

    void registerReceiver(Side s2c, ResourceLocation location, BiConsumer<FriendlyByteBuf, INetworkContext> handler);

    interface INetworkContext {

        void queue(Runnable action);

        Player getPlayer();
        
    }

}
