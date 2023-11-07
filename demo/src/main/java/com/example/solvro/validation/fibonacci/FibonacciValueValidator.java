package com.example.solvro.validation.fibonacci;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FibonacciValueValidator implements ConstraintValidator<FibonacciValue, Integer> {
    @Override
    public void initialize(FibonacciValue constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value <= 1) {
            return true;
        }

        int previous = 0;
        int current = 1;

        while (current < value) {
            int next = previous + current;
            previous = current;
            current = next;
        }

        return current == value;
    }
}
