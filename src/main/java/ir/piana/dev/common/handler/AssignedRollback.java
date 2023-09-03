package ir.piana.dev.common.handler;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AssignedRollback {
    int matchedOrder();
    int order();
}
