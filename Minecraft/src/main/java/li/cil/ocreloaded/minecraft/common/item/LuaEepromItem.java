package li.cil.ocreloaded.minecraft.common.item;

import java.io.InputStream;
import java.util.Optional;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import li.cil.ocreloaded.core.machine.MachineCodeRegistry;
import li.cil.ocreloaded.core.machine.architecture.component.EepromComponent;
import li.cil.ocreloaded.core.machine.architecture.component.base.Component;

public class LuaEepromItem extends EepromItem {

    private static final Logger LOGGER = LoggerFactory.getLogger(LuaEepromItem.class);
    
    public LuaEepromItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component initComponent() {
        Optional<Supplier<Optional<InputStream>>> supplier = MachineCodeRegistry.getDefaultInstance().getBiosCodeSupplier("lua");
        if (!supplier.isPresent()) {
            LOGGER.error("Failed to load Lua BIOS code.");
            return new EepromComponent("");
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

        return new EepromComponent(code);
    }
    
}