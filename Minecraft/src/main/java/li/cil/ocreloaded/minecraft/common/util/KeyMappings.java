package li.cil.ocreloaded.minecraft.common.util;

import java.util.Map;
import java.util.AbstractMap;

public final class KeyMappings {

    private KeyMappings() {}

    // Auto-generated with GPT 4o
    public static final Map<Integer, Integer> GLFW_TO_LWJGL2 = Map.ofEntries(
        // Alphanumeric Keys
        entry(32, 0x39), // GLFW_KEY_SPACE -> KEY_SPACE
        entry(39, 0x28), // GLFW_KEY_APOSTROPHE -> KEY_APOSTROPHE
        entry(44, 0x33), // GLFW_KEY_COMMA -> KEY_COMMA
        entry(45, 0x0C), // GLFW_KEY_MINUS -> KEY_MINUS
        entry(46, 0x34), // GLFW_KEY_PERIOD -> KEY_PERIOD
        entry(47, 0x35), // GLFW_KEY_SLASH -> KEY_SLASH
        entry(48, 0x0B), // GLFW_KEY_0 -> KEY_0
        entry(49, 0x02), // GLFW_KEY_1 -> KEY_1
        entry(50, 0x03), // GLFW_KEY_2 -> KEY_2
        entry(51, 0x04), // GLFW_KEY_3 -> KEY_3
        entry(52, 0x05), // GLFW_KEY_4 -> KEY_4
        entry(53, 0x06), // GLFW_KEY_5 -> KEY_5
        entry(54, 0x07), // GLFW_KEY_6 -> KEY_6
        entry(55, 0x08), // GLFW_KEY_7 -> KEY_7
        entry(56, 0x09), // GLFW_KEY_8 -> KEY_8
        entry(57, 0x0A), // GLFW_KEY_9 -> KEY_9
        entry(59, 0x27), // GLFW_KEY_SEMICOLON -> KEY_SEMICOLON
        entry(61, 0x0D), // GLFW_KEY_EQUAL -> KEY_EQUALS
        entry(65, 0x1E), // GLFW_KEY_A -> KEY_A
        entry(66, 0x30), // GLFW_KEY_B -> KEY_B
        entry(67, 0x2E), // GLFW_KEY_C -> KEY_C
        entry(68, 0x20), // GLFW_KEY_D -> KEY_D
        entry(69, 0x12), // GLFW_KEY_E -> KEY_E
        entry(70, 0x21), // GLFW_KEY_F -> KEY_F
        entry(71, 0x22), // GLFW_KEY_G -> KEY_G
        entry(72, 0x23), // GLFW_KEY_H -> KEY_H
        entry(73, 0x17), // GLFW_KEY_I -> KEY_I
        entry(74, 0x24), // GLFW_KEY_J -> KEY_J
        entry(75, 0x25), // GLFW_KEY_K -> KEY_K
        entry(76, 0x26), // GLFW_KEY_L -> KEY_L
        entry(77, 0x32), // GLFW_KEY_M -> KEY_M
        entry(78, 0x31), // GLFW_KEY_N -> KEY_N
        entry(79, 0x18), // GLFW_KEY_O -> KEY_O
        entry(80, 0x19), // GLFW_KEY_P -> KEY_P
        entry(81, 0x10), // GLFW_KEY_Q -> KEY_Q
        entry(82, 0x13), // GLFW_KEY_R -> KEY_R
        entry(83, 0x1F), // GLFW_KEY_S -> KEY_S
        entry(84, 0x14), // GLFW_KEY_T -> KEY_T
        entry(85, 0x16), // GLFW_KEY_U -> KEY_U
        entry(86, 0x2F), // GLFW_KEY_V -> KEY_V
        entry(87, 0x11), // GLFW_KEY_W -> KEY_W
        entry(88, 0x2D), // GLFW_KEY_X -> KEY_X
        entry(89, 0x15), // GLFW_KEY_Y -> KEY_Y
        entry(90, 0x2C), // GLFW_KEY_Z -> KEY_Z

        // Brackets & Special Characters
        entry(91, 0x1A), // GLFW_KEY_LEFT_BRACKET -> KEY_LBRACKET
        entry(92, 0x2B), // GLFW_KEY_BACKSLASH -> KEY_BACKSLASH
        entry(93, 0x1B), // GLFW_KEY_RIGHT_BRACKET -> KEY_RBRACKET
        entry(96, 0x29), // GLFW_KEY_GRAVE_ACCENT -> KEY_GRAVE

        // Function Keys
        entry(256, 0x01), // GLFW_KEY_ESCAPE -> KEY_ESCAPE
        entry(257, 0x1C), // GLFW_KEY_ENTER -> KEY_RETURN
        entry(258, 0x0F), // GLFW_KEY_TAB -> KEY_TAB
        entry(259, 0x0E), // GLFW_KEY_BACKSPACE -> KEY_BACK
        entry(260, 0xD2), // GLFW_KEY_INSERT -> KEY_INSERT
        entry(261, 0xD3), // GLFW_KEY_DELETE -> KEY_DELETE
        entry(262, 0xCD), // GLFW_KEY_RIGHT -> KEY_RIGHT
        entry(263, 0xCB), // GLFW_KEY_LEFT -> KEY_LEFT
        entry(264, 0xD0), // GLFW_KEY_DOWN -> KEY_DOWN
        entry(265, 0xC8), // GLFW_KEY_UP -> KEY_UP
        entry(266, 0xC9), // GLFW_KEY_PAGE_UP -> KEY_PRIOR
        entry(267, 0xD1), // GLFW_KEY_PAGE_DOWN -> KEY_NEXT
        entry(268, 0xC7), // GLFW_KEY_HOME -> KEY_HOME
        entry(269, 0xCF), // GLFW_KEY_END -> KEY_END
        entry(280, 0x3A), // GLFW_KEY_CAPS_LOCK -> KEY_CAPITAL
        entry(281, 0x46), // GLFW_KEY_SCROLL_LOCK -> KEY_SCROLL
        entry(282, 0x45), // GLFW_KEY_NUM_LOCK -> KEY_NUMLOCK
        entry(283, 0xB7), // GLFW_KEY_PRINT_SCREEN -> KEY_SYSRQ
        entry(284, 0xC5), // GLFW_KEY_PAUSE -> KEY_PAUSE
        entry(290, 0x3B), // GLFW_KEY_F1 -> KEY_F1
        entry(291, 0x3C), // GLFW_KEY_F2 -> KEY_F2
        entry(292, 0x3D), // GLFW_KEY_F3 -> KEY_F3
        entry(293, 0x3E), // GLFW_KEY_F4 -> KEY_F4
        entry(294, 0x3F), // GLFW_KEY_F5 -> KEY_F5
        entry(295, 0x40), // GLFW_KEY_F6 -> KEY_F6
        entry(296, 0x41), // GLFW_KEY_F7 -> KEY_F7
        entry(297, 0x42), // GLFW_KEY_F8 -> KEY_F8
        entry(298, 0x43), // GLFW_KEY_F9 -> KEY_F9
        entry(299, 0x44), // GLFW_KEY_F10 -> KEY_F10
        entry(300, 0x57), // GLFW_KEY_F11 -> KEY_F11
        entry(301, 0x58), // GLFW_KEY_F12 -> KEY_F12

        // Modifier Keys
        entry(340, 0x2A), // GLFW_KEY_LEFT_SHIFT -> KEY_LSHIFT
        entry(341, 0x1D), // GLFW_KEY_LEFT_CONTROL -> KEY_LCONTROL
        entry(342, 0x38), // GLFW_KEY_LEFT_ALT -> KEY_LMENU
        entry(344, 0x36), // GLFW_KEY_RIGHT_SHIFT -> KEY_RSHIFT
        entry(345, 0x9D), // GLFW_KEY_RIGHT_CONTROL -> KEY_RCONTROL
        entry(346, 0xB8), // GLFW_KEY_RIGHT_ALT -> KEY_RMENU
        entry(347, 0xDB), // GLFW_KEY_RIGHT_SUPER -> KEY_LMETA
        entry(348, 0xDD)  // GLFW_KEY_MENU -> KEY_APPS
    );

    public static int translateKeyCode(final int glfwKeyCode) {
        return GLFW_TO_LWJGL2.getOrDefault(glfwKeyCode, glfwKeyCode);
    }

    private static AbstractMap.SimpleEntry<Integer, Integer> entry(int k, int v) {
        return new AbstractMap.SimpleEntry<>(k, v);
    }
    
}
