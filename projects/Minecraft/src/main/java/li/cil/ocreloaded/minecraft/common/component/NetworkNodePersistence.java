package li.cil.ocreloaded.minecraft.common.component;

import java.util.UUID;

import li.cil.ocreloaded.core.machine.PersistenceHolder;
import li.cil.ocreloaded.core.network.NetworkNode;

public final class NetworkNodePersistence {

    private static final String INTERNAL_ID = "INTERNAL_ID";

    private NetworkNodePersistence() {
    }

    public static UUID loadIdOrRandom(PersistenceHolder holder) {
        if (hasId(holder)) return holder.loadUUID(INTERNAL_ID);
        return UUID.randomUUID();
    }

    public static boolean hasId(PersistenceHolder holder) {
        return holder.hasKey(INTERNAL_ID);
    }

    public static void saveId(PersistenceHolder holder, UUID id) {
        holder.storeUUID(INTERNAL_ID, id);
    }

    public static void saveId(PersistenceHolder holder, NetworkNode node) {
        saveId(holder, node.id());
    }
}
