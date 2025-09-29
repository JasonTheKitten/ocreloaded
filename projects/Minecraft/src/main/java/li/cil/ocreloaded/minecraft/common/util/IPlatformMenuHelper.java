package li.cil.ocreloaded.minecraft.common.util;

import java.util.ServiceLoader;
import java.util.function.Consumer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;

public interface IPlatformMenuHelper {
    
    IPlatformMenuHelper INSTANCE = ServiceLoader.load(IPlatformMenuHelper.class).findFirst().orElseThrow();

    void openExtendedMenu(ServerPlayer serverPlayer, MenuProvider menuProvider, Consumer<FriendlyByteBuf> byteBufConsumer);

}