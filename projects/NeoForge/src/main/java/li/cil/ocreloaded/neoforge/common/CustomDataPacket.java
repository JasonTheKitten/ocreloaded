package li.cil.ocreloaded.neoforge.common;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class CustomDataPacket<T> implements CustomPacketPayload {

    private Type<? extends CustomPacketPayload> type;
    private T data;

    public CustomDataPacket(Type<? extends CustomPacketPayload> type, T data) {
        this.type = type;
        this.data = data;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return type;
    }

    public T data() {
        return data;
    }
    
}
