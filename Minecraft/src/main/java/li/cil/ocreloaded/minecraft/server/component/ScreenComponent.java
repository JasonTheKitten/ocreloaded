package li.cil.ocreloaded.minecraft.server.component;

import java.util.UUID;

import li.cil.ocreloaded.core.component.GraphicsBindableComponent;
import li.cil.ocreloaded.core.graphics.TextModeBuffer;
import li.cil.ocreloaded.core.machine.component.AnnotatedComponent;

public class ScreenComponent extends AnnotatedComponent implements GraphicsBindableComponent {

    private final TextModeBuffer screenBuffer;
    
    public ScreenComponent(UUID id, TextModeBuffer screenBuffer) {
        super("screen", id);
        this.screenBuffer = screenBuffer;
    }

    @Override
    public TextModeBuffer getScreenBuffer() {
        return this.screenBuffer;
    }

}