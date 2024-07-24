package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.validator.annotation.ReleaseDate;

import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, LocalDate> {
    private static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext context) {
        if (localDate != null) {
            return !localDate.isBefore(FILM_BIRTHDAY);
        }
        return true;
    }
}
