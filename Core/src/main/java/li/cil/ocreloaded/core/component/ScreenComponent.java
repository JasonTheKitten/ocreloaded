package li.cil.ocreloaded.core.component;

import java.util.List;
import java.util.function.Supplier;

import li.cil.ocreloaded.core.graphics.TextModeBuffer;
import li.cil.ocreloaded.core.machine.component.AnnotatedComponent;
import li.cil.ocreloaded.core.machine.component.ComponentCall.ComponentCallResult;
import li.cil.ocreloaded.core.machine.component.ComponentCallArguments;
import li.cil.ocreloaded.core.machine.component.ComponentCallContext;
import li.cil.ocreloaded.core.machine.component.ComponentMethod;

public class ScreenComponent extends AnnotatedComponent implements GraphicsBindableComponent {

    private final Supplier<TextModeBuffer> screenBufferSupplier;
    
    public ScreenComponent(Supplier<TextModeBuffer> screenBufferSupplier) {
        super("screen");
        this.screenBufferSupplier = screenBufferSupplier;
    }

    @Override
    public TextModeBuffer getScreenBuffer() {
        return screenBufferSupplier.get();
    }

    @ComponentMethod(doc = "function():table -- The list of keyboards attached to the screen.")
    public ComponentCallResult getKeyboards(ComponentCallContext context, ComponentCallArguments arguments) {
        // Just push an empty table for now.
        return ComponentCallResult.success(List.of());
    }

}