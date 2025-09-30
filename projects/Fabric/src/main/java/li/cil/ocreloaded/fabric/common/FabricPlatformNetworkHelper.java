package li.cil.ocreloaded.fabric.common;

import java.util.List;
import java.util.function.BiConsumer;

import io.netty.buffer.Unpooled;
import li.cil.ocreloaded.minecraft.common.network.IPlatformNetworkHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class FabricPlatformNetworkHelper implements IPlatformNetworkHelper {

    @Override
    public void sendToServer(ResourceLocation location, FriendlyByteBuf buffer) {
        ClientPlayNetworking.send(new ByteBasedPayload(location, buffer));
    }

    @Override
    public void sendToPlayer(ServerPlayer serverPlayer, ResourceLocation location, FriendlyByteBuf buffer) {
        ServerPlayNetworking.send(serverPlayer, new ByteBasedPayload(location, buffer));
    }

    @Override
    public void sendToPlayers(List<ServerPlayer> players, ResourceLocation location, FriendlyByteBuf buffer) {
        for (ServerPlayer player: players) {
            sendToPlayer(player, location, buffer);
        }
    }

    @Override
    public void registerReceiver(Side side, ResourceLocation resourceLocation, BiConsumer<FriendlyByteBuf, INetworkContext> handler) {
        Type<ByteBasedPayload> packetType = new Type<>(resourceLocation);

        PayloadTypeRegistry<RegistryFriendlyByteBuf> relevantRegistry  = side == Side.C2S ? PayloadTypeRegistry.playC2S() : PayloadTypeRegistry.playS2C();
        relevantRegistry.register(packetType, StreamCodec.of((dest, source) -> {
            dest.writeBytes(source.buffer());
        }, s -> {
            FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
            byteBuf.writeBytes(s);
            return new ByteBasedPayload(resourceLocation, byteBuf);
        }));

        if (side == Side.C2S) {
            registerServerReceiver(handler, packetType);
        } else if (side == Side.S2C) {
            registerClientReceiver(handler, packetType);
        }
    }

    private void registerServerReceiver(BiConsumer<FriendlyByteBuf, INetworkContext> handler, Type<ByteBasedPayload> packetType) {
        ServerPlayNetworking.registerGlobalReceiver(packetType, (payload, context) -> {
            handler.accept(payload.buffer(), new INetworkContext() {

                @Override
                public void queue(Runnable action) {
                    action.run();
                }

                @Override
                public Player getPlayer() {
                    return context.player();
                }

            });
        });
    }

    private void registerClientReceiver(BiConsumer<FriendlyByteBuf, INetworkContext> handler, Type<ByteBasedPayload> packetType) {
        ClientPlayNetworking.registerGlobalReceiver(packetType, (payload, context) -> {
            handler.accept(payload.buffer(), new INetworkContext() {

                @Override
                public void queue(Runnable action) {
                    action.run();
                }

                @Override
                public Player getPlayer() {
                    return context.player();
                }

            });
        });
    }

    private static class ByteBasedPayload implements CustomPacketPayload {

        private final Type<ByteBasedPayload> type;
        private final FriendlyByteBuf buffer;

        public ByteBasedPayload(ResourceLocation resourceLocation, FriendlyByteBuf buffer) {
            this.type = new Type<>(resourceLocation);
            this.buffer = buffer;
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return type;
        }

        public FriendlyByteBuf buffer() {
            return this.buffer;
        }
    
    }
    
}
