package li.cil.ocreloaded.minecraft.common.network.packets;

import java.nio.ByteBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.common.network.IPlatformNetworkHelper.PacketContext;
import li.cil.ocreloaded.minecraft.common.network.IPlatformNetworkHelper.Side;
import li.cil.ocreloaded.minecraft.common.network.NetworkUtil;
import li.cil.ocreloaded.minecraft.common.registry.ClientBridge;
import li.cil.ocreloaded.minecraft.common.sound.SoundPlayerProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public record SoundPacket(BlockPos pos, int channel, byte[] deltaBuffer) {
    public static final ResourceLocation CHANNEL = ResourceLocation.fromNamespaceAndPath(OCReloadedCommon.MOD_ID, "sound_packet");
    public static final StreamCodec<RegistryFriendlyByteBuf, SoundPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SoundPacket::pos,
            ByteBufCodecs.INT,
            SoundPacket::channel,
            ByteBufCodecs.BYTE_ARRAY,
            SoundPacket::deltaBuffer,
            SoundPacket::new);
    public static final int BEEP_CHANNEL = 0;

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void handle(PacketContext<SoundPacket> ctx) {
        if (!ctx.side().equals(Side.S2C)) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        SoundPacket packet = ctx.message();
        Level level = player.level();
        if (!level.isLoaded(packet.pos)) return;

        if (packet.channel != BEEP_CHANNEL) {
            NetworkUtil.LOGGER.warn("Received unknown channel: {}", packet.channel);
            return;
        }

        ByteBuffer buffer = ByteBuffer.wrap(packet.deltaBuffer);

        short frequency = buffer.getShort();
        short duration = buffer.getShort();

        ClientBridge.getInstance().getClient(SoundPlayerProvider.class).ifPresent(soundPlayerProvider -> soundPlayerProvider.playBeepSound(packet.pos, frequency, duration));
    }

    public static SoundPacket createBeepMessage(BlockPos position, short frequency, short duration) {
        ByteBuf buffer = Unpooled.buffer();

        try {
            buffer.writeShort(frequency);
            buffer.writeShort(duration);
            byte[] deltas = buffer.array().clone();

            return new SoundPacket(position, BEEP_CHANNEL, deltas);
        } finally {
            buffer.release();
        }
    }
}
