package ru.krik.model.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.krik.model.OperationType;

import java.util.Arrays;
import java.util.List;

public class OperationTypeValidator implements ConstraintValidator<ValidOperationType, OperationType> {

    private List<OperationType> acceptedOperationTypes;

    @Override
    public void initialize(ValidOperationType constraintAnnotation) {
        acceptedOperationTypes = Arrays.asList(OperationType.values());
    }

    @Override
    public boolean isValid(OperationType contactField, ConstraintValidatorContext context) {
        return acceptedOperationTypes.contains(contactField);
    }
}
