package org.example.bookstore.annotation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Objects;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Class<?> clazz = value.getClass();
        try {
            Field firstField = clazz.getDeclaredField(firstFieldName);
            firstField.setAccessible(true);
            Object firstObj = firstField.get(value);

            Field secondField = clazz.getDeclaredField(secondFieldName);
            secondField.setAccessible(true);
            Object secondObj = secondField.get(value);

            return Objects.equals(firstObj, secondObj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}
