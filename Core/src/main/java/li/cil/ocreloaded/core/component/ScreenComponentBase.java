package li.cil.ocreloaded.core.component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import li.cil.ocreloaded.core.graphics.TextModeBuffer;
import li.cil.ocreloaded.core.machine.component.AnnotatedComponent;
import li.cil.ocreloaded.core.machine.component.ComponentCall.ComponentCallResult;
import li.cil.ocreloaded.core.machine.component.ComponentCallArguments;
import li.cil.ocreloaded.core.machine.component.ComponentCallContext;
import li.cil.ocreloaded.core.machine.component.ComponentMethod;
import li.cil.ocreloaded.core.network.NetworkNode;

public class ScreenComponentBase extends AnnotatedComponent implements GraphicsBindableComponent {

    private final Supplier<TextModeBuffer> screenBufferSupplier;

    private boolean preciseMode = false;
    
    public ScreenComponentBase(NetworkNode node, Supplier<TextModeBuffer> screenBufferSupplier) {
        super("screen", node);
        this.screenBufferSupplier = screenBufferSupplier;
    }

    @Override
    public TextModeBuffer getScreenBuffer() {
        return screenBufferSupplier.get();
    }

    @ComponentMethod(direct = true, doc = "function():number, number -- The aspect ratio of the screen. For multi-block screens this is the number of blocks, horizontal and vertical.")
    public ComponentCallResult getAspectRatio(ComponentCallContext context, ComponentCallArguments arguments) {
        // TODO: Implement multi-block screen support.
        return ComponentCallResult.success(1, 1);
    }

    @ComponentMethod(doc = "function():table -- The list of keyboards attached to the screen.")
    public ComponentCallResult getKeyboards(ComponentCallContext context, ComponentCallArguments arguments) {
        List<String> keyboards = new ArrayList<>();
        context.networkNode().reachableNodes().forEach(node -> {
            if (node.component().isPresent() && node.component().get() instanceof KeyboardComponent) {
                keyboards.add(node.id().toString());
            }
        });
        return ComponentCallResult.success(keyboards);
    }

    @ComponentMethod(direct = true, doc = "function():boolean -- Returns whether the screen is in high precision mode (sub-pixel mouse event positions).")
    public ComponentCallResult isPrecise(ComponentCallContext context, ComponentCallArguments arguments) {
        return ComponentCallResult.success(preciseMode);
    }

    @ComponentMethod(doc = "function(enabled:boolean):boolean -- Set whether to use high precision mode (sub-pixel mouse event positions).")
    public ComponentCallResult setPrecise(ComponentCallContext context, ComponentCallArguments arguments) {
        if (screenBufferSupplier.get().getDepth() == 8) {
            boolean oldPreciseMode = preciseMode;
            preciseMode = arguments.checkBoolean(0);
            return ComponentCallResult.success(oldPreciseMode);
        } else {
            return ComponentCallResult.failure("unsupported operation");
        }
    }

    public boolean isPrecise() {
        return preciseMode;
    }

}