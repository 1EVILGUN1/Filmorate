package filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import filmorate.annotation.MinimalDate;

import java.time.LocalDate;

public class MinimalDateValidator implements ConstraintValidator<MinimalDate, LocalDate> {
    private LocalDate minimalDate;

    @Override
    public void initialize(MinimalDate constraintAnnotation) {
        minimalDate = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate == null || !localDate.isBefore(minimalDate);
    }
}
