package li.cil.ocreloaded.core.component;

import java.util.HashMap;
import java.util.Map;

import li.cil.ocreloaded.core.machine.component.AnnotatedComponent;
import li.cil.ocreloaded.core.network.NetworkMessage;
import li.cil.ocreloaded.core.network.NetworkNode;
import net.minecraft.world.entity.player.Player;

public class KeyboardComponent extends AnnotatedComponent {

    private final Map<Integer, Character> pressedKeys = new HashMap<>();

    public KeyboardComponent(NetworkNode node) {
        super("keyboard", node);
    }

    @Override
    public void onMessage(NetworkMessage message, NetworkNode sender) {
        NetworkNode node = getNetworkNode();
        switch (message.name()) {
            // TODO: Fix player name
            case "keyboard.keyDown": {
                Player player = null;//(Player) message.arguments()[0];
                int charCode = (int) message.arguments()[1];
                int keyCode = (int) message.arguments()[2];
                pressedKeys.put(keyCode, (char) charCode);
                String playerName = "";// player.getName().getString();
                node.sendToReachable(new NetworkMessage("computer.checked_signal", player, "key_down", charCode, keyCode, playerName));
                break;
            }
            case "keyboard.keyUp": {
                Player player = null;//(Player) message.arguments()[0];
                int keyCode = (int) message.arguments()[1];
                Character charCode = pressedKeys.remove(keyCode);
                if (charCode != null) {
                    String playerName = "";//player.getName().getString();
                    node.sendToReachable(new NetworkMessage("computer.checked_signal", player, "key_up", (int) charCode, keyCode, playerName));
                }
                break;
            }
            case "keyboard.clipboard": {
                Player player = null;
                String clipboard = (String) message.arguments()[1];
                if (clipboard != null) {
                    String playerName = "";//player.getName().getString();
                    node.sendToReachable(new NetworkMessage("computer.checked_signal", player, "clipboard", clipboard, playerName));
                }
            }
            default:
                break;
        }
    }

}