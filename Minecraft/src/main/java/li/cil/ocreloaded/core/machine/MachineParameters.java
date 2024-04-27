package li.cil.ocreloaded.core.machine;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public record MachineParameters(String id, Supplier<Optional<InputStream>> codeStreamSupplier, Supplier<List<Component>> componentScanner) {
    
}
