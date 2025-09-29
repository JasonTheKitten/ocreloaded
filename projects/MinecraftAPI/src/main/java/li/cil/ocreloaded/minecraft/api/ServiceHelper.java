package li.cil.ocreloaded.minecraft.api;

import java.util.ServiceLoader;

public final class ServiceHelper {
    
    private ServiceHelper() {}

    public static <T> T loadService(Class<T> serviceCls) {
        return ServiceLoader.load(serviceCls).findFirst().orElseThrow();
    }

}
