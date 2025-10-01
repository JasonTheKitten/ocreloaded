package li.cil.ocreloaded.minecraft.common.network.packets;

import li.cil.ocreloaded.core.graphics.TextModeBuffer;
import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.common.entity.ScreenBlockEntity;
import li.cil.ocreloaded.minecraft.common.network.IPlatformNetworkHelper.PacketContext;
import li.cil.ocreloaded.minecraft.common.network.IPlatformNetworkHelper.Side;
import li.cil.ocreloaded.minecraft.common.network.NetworkUtil;
import li.cil.ocreloaded.minecraft.common.network.packets.screen.NetworkedTextModeBufferProxy;
import li.cil.ocreloaded.minecraft.common.network.packets.screen.ScreenNetworkInputMessages;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public record ScreenPacket(BlockPos pos, int channel, byte[] deltaBuffer) {
    public static final ResourceLocation CHANNEL = ResourceLocation.fromNamespaceAndPath(OCReloadedCommon.MOD_ID, "screen_packet");
    public static final StreamCodec<RegistryFriendlyByteBuf, ScreenPacket> STREAM_CODEC = StreamCodec.composite(
        BlockPos.STREAM_CODEC, ScreenPacket::pos,
        ByteBufCodecs.INT, ScreenPacket::channel,
        ByteBufCodecs.BYTE_ARRAY, ScreenPacket::deltaBuffer,
        ScreenPacket::new);

    public static final int TEXT_MODE_BUFFER_CHANNEL = 0;
    public static final int INPUT_CHANNEL = 1;

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void handle(PacketContext<ScreenPacket> ctx) {
        ScreenPacket packet = ctx.message();

        Player player = ctx.side().equals(Side.S2C) ?
            Minecraft.getInstance().player :
            ctx.sender();
        if (player == null) return;

        Level level = player.level();

        if (!level.isLoaded(packet.pos)) return;
        if (!(level.getBlockEntity(packet.pos) instanceof ScreenBlockEntity entity)) return;

        if (ctx.side().equals(Side.S2C)) {
            if (packet.channel == TEXT_MODE_BUFFER_CHANNEL)
                writeScreenChanges(entity.getScreenBuffer(), packet.deltaBuffer);
            else nagAboutUnknownChannel(packet.channel);
        } else {
            if (packet.channel == INPUT_CHANNEL)
                ScreenNetworkInputMessages.handleInput(entity, packet.deltaBuffer, player);
            else nagAboutUnknownChannel(packet.channel);
        }
    }

    private static void nagAboutUnknownChannel(int channel) {
        NetworkUtil.LOGGER.warn("Received unknown channel: {}", channel);
    }

    private static void writeScreenChanges(TextModeBuffer screenBuffer, byte[] changeBuffer) {
        NetworkedTextModeBufferProxy.writeTextModeBuffer(screenBuffer, changeBuffer);
    }
}
