package li.cil.ocreloaded.minecraft.server.component;

import java.util.function.Supplier;

import li.cil.ocreloaded.core.component.GraphicsBindableComponent;
import li.cil.ocreloaded.core.graphics.TextModeBuffer;
import li.cil.ocreloaded.core.machine.component.AnnotatedComponent;

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

}