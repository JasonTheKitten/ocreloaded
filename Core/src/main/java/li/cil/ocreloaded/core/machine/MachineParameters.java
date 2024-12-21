package li.cil.ocreloaded.core.machine;

import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import li.cil.ocreloaded.core.network.NetworkNode;

public record MachineParameters(
    NetworkNode networkNode,
    Supplier<Optional<InputStream>> codeStreamSupplier,
    ExecutorService threadService
) {
    
}
