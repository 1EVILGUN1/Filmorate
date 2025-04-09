package filmorate.annotation;

import jakarta.validation.Constraint;
import filmorate.validator.MinimalDateValidator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinimalDateValidator.class)
public @interface MinimalDate {
    String message() default "Дата не может быть раньше {value}";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};

    String value() default "1895-12-28";
}
