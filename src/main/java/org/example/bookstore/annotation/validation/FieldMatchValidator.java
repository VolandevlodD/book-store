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
        try {
            Object firstObj = getFieldValue(value, firstFieldName);
            Object secondObj = getFieldValue(value, secondFieldName);
            return Objects.equals(firstObj, secondObj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }

    private Object getFieldValue(Object value, String fieldName) throws NoSuchFieldException,
            IllegalAccessException {
        Class<?> clazz = value.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(value);
    }
}
