package li.cil.ocreloaded.core.machine;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import li.cil.ocreloaded.core.network.NetworkNode;

public final class MachineBuilder {

    private static final MachineBuilder INSTANCE = new MachineBuilder();

    private final Map<String, MachineArchitecture> architectures = new HashMap<>();

    public void registerArchitecture(
            String id,
            Supplier<Optional<InputStream>> codeStreamSupplier,
            Function<MachineParameters, Optional<Machine>> machineFactory,
            BooleanSupplier supported
    ) {
        architectures.put(id, new MachineArchitecture(codeStreamSupplier, machineFactory, supported));
    }

    public Optional<Machine> createMachine(
            String architecture,
            NetworkNode networkNode,
            NetworkNode tmpFsNode,
            ExecutorService threadService,
            MachineProcessor processor,
            MachineActions actions
    ) {
        MachineArchitecture definition = architectures.get(architecture);
        if (definition == null || !definition.supported().getAsBoolean()) {
            return Optional.empty();
        }

        MachineParameters parameters = new MachineParameters(
                networkNode,
                tmpFsNode,
                definition.codeStreamSupplier(),
                threadService,
                processor,
                actions);

        return definition.machineFactory().apply(parameters);
    }

    public List<String> supportedArchitectures() {
        return architectures.entrySet().stream()
                .filter(entry -> entry.getValue().supported().getAsBoolean())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public boolean isSupportedArchitecture(String architecture) {
        MachineArchitecture definition = architectures.get(architecture);
        return definition != null && definition.supported().getAsBoolean();
    }

    public static MachineBuilder getDefaultInstance() {
        return INSTANCE;
    }

    private record MachineArchitecture(
            Supplier<Optional<InputStream>> codeStreamSupplier,
            Function<MachineParameters, Optional<Machine>> machineFactory,
            BooleanSupplier supported
    ) { }
}
