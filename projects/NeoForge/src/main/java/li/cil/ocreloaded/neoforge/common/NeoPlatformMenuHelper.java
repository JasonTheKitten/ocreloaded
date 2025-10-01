package li.cil.ocreloaded.neoforge.common;

import java.util.function.Consumer;

import li.cil.ocreloaded.minecraft.common.util.IPlatformMenuHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;

public class NeoPlatformMenuHelper implements IPlatformMenuHelper {
    @Override
    public void openExtendedMenu(ServerPlayer serverPlayer, MenuProvider menuProvider, Consumer<FriendlyByteBuf> byteBufConsumer) {
        serverPlayer.openMenu(menuProvider, byteBufConsumer::accept);
    }
}
