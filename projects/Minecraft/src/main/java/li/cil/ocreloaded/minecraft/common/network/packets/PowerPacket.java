package li.cil.ocreloaded.minecraft.common.network.packets;

import commonnetwork.api.Dispatcher;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.common.WorldUtil;
import li.cil.ocreloaded.minecraft.common.entity.CaseBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public record PowerPacket(BlockPos pos, boolean powerState) {
    public static final ResourceLocation CHANNEL = ResourceLocation.fromNamespaceAndPath(OCReloadedCommon.MOD_ID, "power_packet");
    public static final StreamCodec<RegistryFriendlyByteBuf, PowerPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            PowerPacket::pos,
            ByteBufCodecs.BOOL,
            PowerPacket::powerState,
            PowerPacket::new);

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void handle(PacketContext<PowerPacket> ctx) {
        if (ctx.side().equals(Side.CLIENT))
            handleClient(ctx);
        else
            handleServer(ctx);
    }

    private static void handleClient(PacketContext<PowerPacket> ctx) {
        PowerPacket packet = ctx.message();

        BlockPos position = packet.pos;
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        Level level = player.level();

        if (!level.isLoaded(position)) return;
        if (level.getBlockEntity(position) instanceof CaseBlockEntity entity) entity.setPowered(packet.powerState);
    }

    private static void handleServer(PacketContext<PowerPacket> ctx) {
        PowerPacket packet = ctx.message();

        Player player = ctx.sender();
        Level level = player.level();

        if (!level.isLoaded(packet.pos))  return;
        if (!WorldUtil.isPlayerCloseEnough(player, packet.pos)) return;

        if (level.getBlockEntity(packet.pos) instanceof CaseBlockEntity entity) {
            entity.setPowered(packet.powerState);
            Dispatcher.sendToClientsInLevel(packet, (ServerLevel) level);
        }
    }
}
