package kh.com.kshrd.hrdregisterapi.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kh.com.kshrd.hrdregisterapi.annotation.validator.MinAgeValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinAgeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MinAge {
    String message() default "Must be at least {value} years old";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int value();
}

