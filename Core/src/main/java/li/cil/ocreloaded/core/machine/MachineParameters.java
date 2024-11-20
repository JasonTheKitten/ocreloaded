package li.cil.ocreloaded.core.machine;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import li.cil.ocreloaded.core.machine.component.Component;

public record MachineParameters(
    UUID id,
    Supplier<Optional<InputStream>> codeStreamSupplier,
    Supplier<Map<UUID, Component>> componentScanner,
    ExecutorService threadService
) {
    
}
