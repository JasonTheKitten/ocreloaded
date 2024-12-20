package li.cil.ocreloaded.minecraft.common.network.power;

import li.cil.ocreloaded.minecraft.common.WorldUtil;
import li.cil.ocreloaded.minecraft.common.entity.CaseBlockEntity;
import li.cil.ocreloaded.minecraft.common.network.ClientNetworkMessageContext;
import li.cil.ocreloaded.minecraft.common.network.NetworkHandler;
import li.cil.ocreloaded.minecraft.common.network.NetworkUtil;
import li.cil.ocreloaded.minecraft.common.network.ServerNetworkMessageContext;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PowerNetworkHandler implements NetworkHandler<PowerNetworkMessage> {

    @Override
    public String getType() {
        return PowerNetworkMessage.TYPE;
    }

    @Override
    public Class<PowerNetworkMessage> getMessageType() {
        return PowerNetworkMessage.class;
    }

    @Override
    public void encode(FriendlyByteBuf buffer, PowerNetworkMessage message) {
        buffer.writeBlockPos(message.position());
        buffer.writeBoolean(message.powerState());
    }

    @Override
    public PowerNetworkMessage decode(FriendlyByteBuf buffer) {
        return new PowerNetworkMessage(buffer.readBlockPos(), buffer.readBoolean());
    }

    @Override
    public void handleClient(PowerNetworkMessage message, ClientNetworkMessageContext context) {
        BlockPos position = message.position();

        if (context.player() == null) return;
        Level level = context.player().level();
        if (!level.isLoaded(position)) return;
        if (level.getBlockEntity(position) instanceof CaseBlockEntity entity) {
            entity.setPowered(message.powerState());
        }
    }

    @Override
    public void handleServer(PowerNetworkMessage message, ServerNetworkMessageContext context) {
        BlockPos position = message.position();
        Player player = context.sender();
        Level level = player.level();
        if (!level.isLoaded(position))  return;
        if (!WorldUtil.isPlayerCloseEnough(player, position)) return;
        if (level.getBlockEntity(position) instanceof CaseBlockEntity entity) {
            entity.setPowered(message.powerState());
            redistributeMessage(message, level);
        }
    }

    private void redistributeMessage(PowerNetworkMessage message, Level level) {
        for (Player otherPlayer : level.players()) {
            NetworkUtil.getInstance().messageClient(message, otherPlayer);
        }
    }
    
}
