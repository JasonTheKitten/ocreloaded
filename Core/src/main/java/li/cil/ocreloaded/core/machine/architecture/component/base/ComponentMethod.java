package li.cil.ocreloaded.core.machine.architecture.component.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A annotation to be put on methods of a {@link Component}
 * every method annotated with this is exposed to the underlying machine
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ComponentMethod {

    /**
     * @return Whether this is a direct call and can skip scheduling.
     */
    boolean direct() default false;

    /**
     * @return The documentation string for this method
     */
    String doc();

    /**
     * @return If the method is a getter
     */
    boolean getter() default false;

    /**
     * @return If the method is a setter
     */
    boolean setter() default false;

}