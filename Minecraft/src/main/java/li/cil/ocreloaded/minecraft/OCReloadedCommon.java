package li.cil.ocreloaded.minecraft;

import java.util.List;

import li.cil.ocreloaded.minecraft.block.BlockInfo;
import li.cil.ocreloaded.minecraft.block.CaseBlockInfo;

public class OCReloadedCommon {

    public static final String MOD_ID = "ocreloaded";
    
    public List<BlockInfo> getBlockInfos() {
        return List.of(new BlockInfo[] {
            new CaseBlockInfo()
        });
    }

}
