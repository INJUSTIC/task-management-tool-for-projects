package com.example.solvro.validation.fibonacci;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FibonacciValueValidator.class)
@Documented
public @interface FibonacciValue {
    String message() default "Value must be in the Fibonacci sequence";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
