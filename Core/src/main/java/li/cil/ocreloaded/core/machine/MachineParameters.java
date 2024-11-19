package li.cil.ocreloaded.core.machine;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import li.cil.ocreloaded.core.machine.architecture.component.base.Component;

public record MachineParameters(
    String id,
    Supplier<Optional<InputStream>> codeStreamSupplier,
    Supplier<Map<UUID, Component>> componentScanner,
    ExecutorService threadService
) {
    
}
