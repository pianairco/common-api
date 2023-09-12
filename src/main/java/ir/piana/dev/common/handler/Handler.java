package ir.piana.dev.common.handler;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Handler {
    @AliasFor(
            annotation = Component.class
    )
    String value() default "";

    String description() default "";

    Class consume() default Object.class;

    FieldDescription[] consumeFields() default {};

    Class produce() default Object.class;

    FieldDescription[] produceFields() default {};

    @interface FieldDescription {
        String name();
        String type();
        String pattern() default "";
        String description() default "";
    }
}
