package li.cil.ocreloaded.minecraft.common.item;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import li.cil.ocreloaded.core.component.EepromComponent;
import li.cil.ocreloaded.core.network.NetworkNode;
import li.cil.ocreloaded.core.network.NetworkNode.Visibility;
import li.cil.ocreloaded.core.network.NetworkNodes;
import net.minecraft.world.item.Item;

public class EepromItem extends Item implements ComponentItem {
    private static final Logger LOGGER = LoggerFactory.getLogger(EepromItem.class);

    private final String defaultCode;

    public EepromItem(Properties properties) {
        this(properties, "");
    }

    public EepromItem(Properties properties, String defaultCode) {
        super(properties);
        this.defaultCode = defaultCode;
    }

    @Override
    public NetworkNode newNetworkNode(UUID id) {
        return NetworkNodes.component(id, node -> new EepromComponent(node, defaultCode), Visibility.NEIGHBORS);
    }

    public static String read(String path) {
        try (InputStream inputStream = EepromItem.class.getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                LOGGER.error("Failed to load EEPROM code from {}.", path);
                return "";
            }

            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOGGER.error("Failed to load EEPROM code from {}.", path, e);
            return "";
        }
    }
}
