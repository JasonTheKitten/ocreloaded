package li.cil.ocreloaded.minecraft.block;

public interface BlockInfo {

    String blockName();

    default boolean hasCreativeTab() {
        return true;
    }

    default float strength() {
        return 2.0f;
    };
    
}
