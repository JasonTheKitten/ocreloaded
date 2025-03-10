package li.cil.ocreloaded.core.component;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import li.cil.ocreloaded.core.graphics.TextModeBuffer;
import li.cil.ocreloaded.core.graphics.color.ColorMode.ColorData;
import li.cil.ocreloaded.core.machine.component.AnnotatedComponent;
import li.cil.ocreloaded.core.machine.component.ComponentCall.ComponentCallResult;
import li.cil.ocreloaded.core.machine.component.ComponentCallArguments;
import li.cil.ocreloaded.core.machine.component.ComponentCallContext;
import li.cil.ocreloaded.core.machine.component.ComponentMethod;
import li.cil.ocreloaded.core.network.NetworkNode;

//TODO: Call budgets, energy
public class GraphicsCardComponent extends AnnotatedComponent {

    private static final int RESERVED_SCREEN_INDEX = 0;

    private final int[] maxResolution;

    private UUID screenId;
    private int currentScreenBuffer = RESERVED_SCREEN_INDEX;
    
    public GraphicsCardComponent(int[] maxResolution) {
        super("gpu");
        this.maxResolution = maxResolution;
    }

    @ComponentMethod(doc = "function(address:string[, reset:boolean=true]):boolean -- Binds the GPU to the screen with the specified address and resets screen settings if `reset` is true.")
    public ComponentCallResult bind(ComponentCallContext context, ComponentCallArguments arguments) {
        String address = arguments.checkString(0);
        boolean reset = arguments.optionalBoolean(1, true);
        Optional<NetworkNode> node = context.networkNode().reachableNode(UUID.fromString(address));

        if (node.isEmpty() || node.get().component().isEmpty()) {
            return ComponentCallResult.success(false, "invalid address");
        }
        if (!(node.get().component().get() instanceof ScreenComponent)) {
            return ComponentCallResult.success(false, "not a screen");
        }
        screenId = node.get().id();

        return withBuffer(context, RESERVED_SCREEN_INDEX, buffer -> {
            if (reset) {
                int[] bufferMaxResolution = buffer.maxResolution();
                buffer.setResolution(
                    Math.min(maxResolution[0], bufferMaxResolution[0]),
                    Math.min(maxResolution[1], bufferMaxResolution[1]));
                buffer.setForegroundColor(0xFFFFFFFF, false);
                buffer.setBackgroundColor(0x00000000, false);
                buffer.reset();
                // TODO: Set color depth
            }
            return ComponentCallResult.success(true);
        });
    }

    @ComponentMethod(direct = true, doc = "function():string -- Get the address of the screen the GPU is currently bound to.")
    public ComponentCallResult getScreen(ComponentCallContext context, ComponentCallArguments arguments) {
        return withBuffer(context, RESERVED_SCREEN_INDEX, buffer -> ComponentCallResult.success(screenId.toString()));
    }

    @ComponentMethod(direct = true, doc = "function():number, boolean -- Get the current background color and whether it's from the palette or not.")
    public ComponentCallResult getBackground(ComponentCallContext context, ComponentCallArguments arguments) {
        return withBuffer(context, buffer -> ComponentCallResult.success(buffer.getBackgroundColor().color(), buffer.getBackgroundColor().isPaletteIndex()));
    }

    @ComponentMethod(direct = true, doc = "function(value:number[, palette:boolean]):number, number or nil -- Sets the background color to the specified value." +
        "Optionally takes an explicit palette index. Returns the old value and if it was from the palette its palette index.")
    public ComponentCallResult setBackground(ComponentCallContext context, ComponentCallArguments arguments) {
        int color = arguments.checkInteger(0);
        boolean isPaletteIndex = arguments.optionalBoolean(1, false);
        return withBuffer(context, buffer -> {
            ColorData oldColor = buffer.getBackgroundColor();
            buffer.setBackgroundColor(color, isPaletteIndex);
            if (oldColor.isPaletteIndex()) {
                return ComponentCallResult.success(buffer.getPaletteColor(oldColor.color()), oldColor.color());
            } else {
                return ComponentCallResult.success(oldColor.color(), null);
            }
        });
    }

    @ComponentMethod(direct = true, doc = "function():number, boolean -- Get the current foreground color and whether it's from the palette or not.")
    public ComponentCallResult getForeground(ComponentCallContext context, ComponentCallArguments arguments) {
        return withBuffer(context, buffer -> ComponentCallResult.success(buffer.getForegroundColor().color(), buffer.getForegroundColor().isPaletteIndex()));
    }

    @ComponentMethod(direct = true, doc = "function(value:number[, palette:boolean]):number, number or nil -- Sets the foreground color to the specified value." +
        "Optionally takes an explicit palette index. Returns the old value and if it was from the palette its palette index.")
    public ComponentCallResult setForeground(ComponentCallContext context, ComponentCallArguments arguments) {
        int color = arguments.checkInteger(0);
        boolean isPaletteIndex = arguments.optionalBoolean(1, false);
        return withBuffer(context, buffer -> {
            ColorData oldColor = buffer.getForegroundColor();
            buffer.setForegroundColor(color, isPaletteIndex);
            if (oldColor.isPaletteIndex()) {
                return ComponentCallResult.success(buffer.getPaletteColor(oldColor.color()), oldColor.color());
            } else {
                return ComponentCallResult.success(oldColor.color(), null);
            }
        });
    }

    @ComponentMethod(direct = true, doc = "function():number -- Returns the currently set color depth.")
    public ComponentCallResult getDepth(ComponentCallContext context, ComponentCallArguments arguments) {
        return withBuffer(context, buffer -> ComponentCallResult.success(buffer.getDepth()));
    }

    @ComponentMethod(doc = "function(depth:number):number -- Set the color depth. Returns the previous value.")
    public ComponentCallResult setDepth(ComponentCallContext context, ComponentCallArguments arguments) {
        // TODO: Implement setDepth
        return withBuffer(context, buffer -> {
            int oldDepth = buffer.getDepth();
            return ComponentCallResult.success(oldDepth);
        });
    }

    @ComponentMethod(direct = true, doc = "function():number, number -- Get the current screen resolution.")
    public ComponentCallResult getResolution(ComponentCallContext context, ComponentCallArguments arguments) {
        return withBuffer(context, buffer -> {
            int[] resolution = buffer.resolution();
            return ComponentCallResult.success(resolution[0], resolution[1]);
        });
    }

    @ComponentMethod(doc = "function(width:number, height:number):boolean -- Set the screen resolution. Returns true if the resolution changed.")
    public ComponentCallResult setResolution(ComponentCallContext context, ComponentCallArguments arguments) {
        int width = arguments.checkInteger(0);
        int height = arguments.checkInteger(1);
        return withBuffer(context, buffer -> {
            int[] resolution = buffer.resolution();
            if (resolution[0] == width && resolution[1] == height) {
                return ComponentCallResult.success(false);
            }
            
            buffer.setResolution(width, height);
            return ComponentCallResult.success(true);
        });
    }

    @ComponentMethod(direct = true, doc = "function():number, number -- Get the maximum screen resolution.")
    public ComponentCallResult maxResolution(ComponentCallContext context, ComponentCallArguments arguments) {
        return withBuffer(context, buffer -> {
            int[] maxBufferResolution = buffer.maxResolution();
            return ComponentCallResult.success(
                Math.min(maxResolution[0], maxBufferResolution[0]),
                Math.min(maxResolution[1], maxBufferResolution[1])
            );
        });
    }

    @ComponentMethod(direct = true, doc = "function():number, number -- Get the current viewport resolution.")
    public ComponentCallResult getViewport(ComponentCallContext context, ComponentCallArguments arguments) {
        return withBuffer(context, buffer -> {
            int[] viewport = buffer.viewport();
            return ComponentCallResult.success(viewport[0], viewport[1]);
        });
    }

    @ComponentMethod(doc = "function(width:number, height:number):boolean -- Set the viewport resolution. Cannot exceed the screen resolution. Returns true if the resolution changed.")
    public ComponentCallResult setViewport(ComponentCallContext context, ComponentCallArguments arguments) {
        int width = arguments.checkInteger(0);
        int height = arguments.checkInteger(1);
        return withBuffer(context, buffer -> {
            int[] resolution = buffer.resolution();
            int[] maxResolution = buffer.maxResolution();
            if (width > resolution[0] || height > resolution[1] || width > maxResolution[0] || height > maxResolution[1] || width < 1 || height < 1) {
                return ComponentCallResult.failure("unsupported viewport size");
            }
            int[] viewport = buffer.viewport();
            if (viewport[0] == width && viewport[1] == height) {
                return ComponentCallResult.success(false);
            }
            buffer.setViewport(width, height);
            return ComponentCallResult.success(true);
        });
    }

    @ComponentMethod(direct = true, doc = "function(x:number, y:number):string, number, number, number or nil, number or nil -- " +
        "Get the value displayed on the screen at the specified index, as well as the foreground and background color. If the foreground " +
        "or background is from the palette, returns the palette indices as fourth and fifth results, else nil, respectively.")
    public ComponentCallResult get(ComponentCallContext context, ComponentCallArguments arguments) {
        int x = arguments.checkInteger(0) - 1;
        int y = arguments.checkInteger(1) - 1;
        return withBuffer(context, buffer -> {
            TextModeBuffer.CellInfo cell = buffer.getTextCell(x, y);
            return ComponentCallResult.success(
                new String(Character.toChars(cell.codepoint())),
                cell.foreground(),
                cell.background(),
                cell.foregroundIndex() == -1 ? null : cell.foregroundIndex(),
                cell.backgroundIndex() == -1 ? null : cell.backgroundIndex()
            );
        });
    }

    @ComponentMethod(direct = true, doc = "function(x:number, y:number, value:string[, vertical:boolean]):boolean -- " +
        "Plots a string value to the screen at the specified position. Optionally writes the string vertically.")
    public ComponentCallResult set(ComponentCallContext context, ComponentCallArguments arguments) {
        int x = arguments.checkInteger(0) - 1;
        int y = arguments.checkInteger(1) - 1;
        String value = arguments.checkString(2);
        boolean vertical = arguments.optionalBoolean(3, false);
        return withBuffer(context, buffer -> {
            buffer.writeText(x, y, value, vertical);
            return ComponentCallResult.success(true);
        });
    }

    @ComponentMethod(direct = true, doc = "function(x:number, y:number, width:number, height:number, tx:number, ty:number):boolean -- " +
        "Copies a portion of the screen from the specified location with the specified size by the specified translation.")
    public ComponentCallResult copy(ComponentCallContext context, ComponentCallArguments arguments) {
        int x = arguments.checkInteger(0) - 1;
        int y = arguments.checkInteger(1) - 1;
        int width = arguments.checkInteger(2);
        int height = arguments.checkInteger(3);
        int tx = arguments.checkInteger(4);
        int ty = arguments.checkInteger(5);
        return withBuffer(context, buffer -> {
            buffer.copy(x, y, width, height, tx, ty);
            return ComponentCallResult.success(true);
        });
    }

    @ComponentMethod(direct = true, doc = "function(x:number, y:number, width:number, height:number, char:string):boolean -- " +
        "Fills a portion of the screen at the specified position with the specified size with the specified character.")
    public ComponentCallResult fill(ComponentCallContext context, ComponentCallArguments arguments) {
        int x = arguments.checkInteger(0) - 1;
        int y = arguments.checkInteger(1) - 1;
        int width = arguments.checkInteger(2);
        int height = arguments.checkInteger(3);
        String charString = arguments.checkString(4);
        if (charString.length() != 1) {
            return ComponentCallResult.failure("invalid fill value");
        }
        int codepoint = charString.codePointAt(0);
        return withBuffer(context, buffer -> {
            buffer.fill(x, y, width, height, codepoint);
            return ComponentCallResult.success(true);
        });
    }

    private ComponentCallResult withBuffer(ComponentCallContext context, int index, Function<TextModeBuffer, ComponentCallResult> function) {
        if (index == RESERVED_SCREEN_INDEX) {
            return Optional.ofNullable(screenId)
                .flatMap(context.networkNode()::reachableNode)
                .flatMap(NetworkNode::component)
                .flatMap(component -> component instanceof ScreenComponent screenComponent ? Optional.of(screenComponent) : Optional.empty())
                .map(ScreenComponent::getScreenBuffer)
                .map(buffer -> { synchronized(buffer) {
                    return function.apply(buffer);
                }})
                .orElse(ComponentCallResult.success(null, "no screen"));
        } else {
            return ComponentCallResult.success(null, "invalid buffer index");
        }
    }

    private ComponentCallResult withBuffer(ComponentCallContext context, Function<TextModeBuffer, ComponentCallResult> function) {
        return withBuffer(context, currentScreenBuffer, function);
    }

}
