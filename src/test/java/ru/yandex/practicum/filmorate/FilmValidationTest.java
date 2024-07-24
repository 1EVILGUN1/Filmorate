package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.group.Create;
import ru.yandex.practicum.filmorate.validator.group.Default;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тестирование модели фильма")
public class FilmValidationTest {
    private Validator validator;
    private Film film;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        film = new Film();
        film.setId(1L);
        film.setName("Test");
        film.setDescription("Some description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(85);
    }

    @Test
    @DisplayName("Тест при корректных данных фильма")
    void shouldSuccessValidationWhenValidFilmData() {
        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class, Default.class);
        assertTrue(violations.isEmpty(), "Ошибка теста при корректных данных фильма");
    }

    @Test
    @DisplayName("Тест при некорректном имени")
    void shouldFailValidationWhenInvalidName() {
        film.setName(" ");
        Set<ConstraintViolation<Film>> violations = validator.validate(film, Create.class);
        assertFalse(violations.isEmpty(), "Некорректное название фильма");
    }

    @Test
    @DisplayName("Пройти тест при описании длинной 200 символов")
    void shouldSuccessValidationWhenDescriptionLengthEquals200() {
        film.setDescription("Имеется спорная точка зрения гласящая примерно следующее тщательные исследования +" +
                "конкурентов освещают интересные особенности картины в целом однако конкретные выводы разумеется +" +
                "описаны макс подробно");

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Default.class);
        assertTrue(violations.isEmpty(), "Ошибка теста при корректном описании фильма");
    }

    @Test
    @DisplayName("Провалить валидацию при описании длинной больше 200 символов")
    void shouldFailValidationWhenDescriptionLengthOver200() {
        film.setDescription("Прежде всего, начало повседневной работы по формированию позиции позволяет оценить +" +
                "значение инновационных методов управления процессами. Однозначно, реплицированные с зарубежных +" +
                "источников, современные исследования неоднозначны и будут ограничены исключительно образом мышления.");

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Default.class);
        assertFalse(violations.isEmpty(), "Некорректное описание фильма");
    }

    @Test
    @DisplayName("Пройти тест при дате релиза в день рождения кино")
    void shouldSuccessValidationWhenReleaseDateIsFilmBirthday() {
        film.setReleaseDate(LocalDate.of(1955, 2, 8));

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Default.class);
        assertTrue(violations.isEmpty(), "Ошибка теста при корректной дате релиза фильма");
    }

    @Test
    @DisplayName("Провалить тест при дате релиза раньше дня рождения кино")
    void shouldFailValidationWhenReleaseDateIsBeforeFilmBirthday() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Default.class);
        assertFalse(violations.isEmpty(), "Некорректная дата релиза фильма");
    }

    @Test
    @DisplayName("Провалить тест при нулевой продолжительности")
    void shouldFailValidationWhenZeroDuration() {
        film.setDuration(0);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Default.class);
        assertFalse(violations.isEmpty(), "Некорректная продолжительность фильма");
    }

    @Test
    @DisplayName("Провалить тест при отрицательной продолжительности")
    void shouldFailValidationWhenNegativeDuration() {
        film.setDuration(-1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Default.class);
        assertFalse(violations.isEmpty(), "Некорректная продолжительность фильма");
    }
}
