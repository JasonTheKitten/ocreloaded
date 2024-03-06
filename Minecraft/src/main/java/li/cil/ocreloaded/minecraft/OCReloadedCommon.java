package li.cil.ocreloaded.minecraft;

import li.cil.ocreloaded.minecraft.block.BlockInfo;
import li.cil.ocreloaded.minecraft.block.CaseBlockInfo;

public class OCReloadedCommon {

    public static final String MOD_ID = "ocreloaded";
    
    public BlockInfo[] getBlockInfos() {
        return new BlockInfo[] {
            new CaseBlockInfo()
        };
    }

}
