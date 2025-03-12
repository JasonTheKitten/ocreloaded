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
import org.slf4j.LoggerFactory;

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


    @ComponentMethod(doc = "function():table -- The list of keybo   ards attached to the screen.")
    public ComponentCallResult getKeyboards(ComponentCallContext context, ComponentCallArguments arguments) {
        List<String> keyboards = new ArrayList<>();
        context.networkNode().reachableNodes().forEach(node -> {
            if (node.component().isPresent() && node.component().get() instanceof KeyboardComponent keyboardComponent) {
                keyboards.add(keyboardComponent.uuid().toString());
            }
        });
        return ComponentCallResult.success(keyboards);
    }

}