package li.cil.ocreloaded.minecraft.common.item;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import li.cil.ocreloaded.core.component.EepromComponent;
import li.cil.ocreloaded.core.machine.MachineCodeRegistry;
import li.cil.ocreloaded.core.machine.component.Component;
import li.cil.ocreloaded.core.network.NetworkNode;
import li.cil.ocreloaded.core.network.NetworkNode.Visibility;
import li.cil.ocreloaded.core.network.NetworkNodes;

public class LuaEepromItem extends EepromItem {
    private static final Logger LOGGER = LoggerFactory.getLogger(LuaEepromItem.class);
    
    public LuaEepromItem(Properties properties) {
        super(properties);
    }

    @Override
    public NetworkNode newNetworkNode(UUID id) {
        return NetworkNodes.component(id, this::initComponent, Visibility.NEIGHBORS);
    }

    private Component initComponent(NetworkNode node) {
        Optional<Supplier<Optional<InputStream>>> supplier = MachineCodeRegistry.getDefaultInstance().getBiosCodeSupplier("lua");
        if (supplier.isEmpty()) {
            LOGGER.error("Failed to load Lua BIOS code.");
            return new EepromComponent(node, "");
        }

        String code = supplier.get().get().map(inputStream -> {
            try {
                return new String(inputStream.readAllBytes());
            } catch (Exception e) {
                return "";
            }
        }).orElse("");

        if (code.isEmpty()) {
            LOGGER.error("Failed to load Lua BIOS code.");
        }

        return new EepromComponent(node, code);
    }
}
