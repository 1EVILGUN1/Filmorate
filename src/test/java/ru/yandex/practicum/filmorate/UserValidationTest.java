package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.group.Create;
import ru.yandex.practicum.filmorate.validator.group.Default;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тестирование модели пользователя")
public class UserValidationTest {
    private Validator validator;
    private User user;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        user = new User();
        user.setId(2L);
        user.setEmail("email@email.com");
        user.setLogin("login");
        user.setBirthday(LocalDate.now().minusMonths(1));
    }

    @Test
    @DisplayName("Пройти тест при корректных данных пользователя")
    void shouldSuccessValidationWhenValidFilmData() {
        Set<ConstraintViolation<User>> violations = validator.validate(user, Create.class, Default.class);
        assertTrue(violations.isEmpty(), "Ошибка при корректных данных пользователя");
    }

    @Test
    @DisplayName("Провалить тест при пустой электронной почте")
    void shouldFailValidationWhenEmptyEmail() {
        user.setEmail(" ");
        Set<ConstraintViolation<User>> violations = validator.validate(user, Default.class);
        assertFalse(violations.isEmpty(), "Некорректный тест при пустой электронной почте");
    }

    @Test
    @DisplayName("Провалить тест при некорректной электронной почте")
    void shouldFailValidationWhenIncorrectEmail() {
        user.setEmail("@test");
        Set<ConstraintViolation<User>> violations = validator.validate(user, Default.class);
        assertFalse(violations.isEmpty(), "Некорректный тест при некорректной электронной почте");
    }

    @Test
    @DisplayName("Провалить тест при пустом логине")
    void shouldFailValidationWhenEmptyLogin() {
        user.setLogin(" ");
        Set<ConstraintViolation<User>> violations = validator.validate(user, Default.class);
        assertFalse(violations.isEmpty(), "Некорректный тест при пустом логине");
    }

    @Test
    @DisplayName("Провалить тест при логине c пробелами")
    void shouldFailValidationWhenLoginHasWhiteSpace() {
        user.setLogin("Gabriel Angel");
        Set<ConstraintViolation<User>> violations = validator.validate(user, Default.class);
        assertFalse(violations.isEmpty(), "Некорректный тест при логине с пробелами");
    }

    @Test
    @DisplayName("Пройти тест при дате рождения в настоящем")
    void shouldSuccessValidationWhenPresentBirthDay() {
        user.setBirthday(LocalDate.now());
        Set<ConstraintViolation<User>> violations = validator.validate(user, Default.class);
        assertTrue(violations.isEmpty(), "Ошибка тест при корректной дате рождения");
    }

    @Test
    @DisplayName("Провалить тест при дате рождения в будущем")
    void shouldFailValidationWhenFutureBirthDay() {
        user.setBirthday(LocalDate.now().plusMonths(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user, Default.class);
        assertFalse(violations.isEmpty(), "Некорректная тест при дате рождения в будущем");
    }
}
