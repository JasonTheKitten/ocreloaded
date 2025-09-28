package li.cil.ocreloaded.minecraft.common.network.power;

import li.cil.ocreloaded.minecraft.common.network.NetworkMessage;
import net.minecraft.core.BlockPos;

public class PowerNetworkMessage implements NetworkMessage {

    public static final String TYPE = "power";
    
    private final BlockPos position;
    private final boolean powerState;

    public PowerNetworkMessage(BlockPos position, boolean powerState) {
        this.position = position;
        this.powerState = powerState;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public BlockPos position() {
        return position;
    }

    public boolean powerState() {
        return powerState;
    }

}
