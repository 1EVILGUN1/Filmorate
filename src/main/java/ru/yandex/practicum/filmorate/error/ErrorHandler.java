package ru.yandex.practicum.filmorate.error;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(final ValidationException exception) {
        log.warn(exception.getMessage(), exception);
        return new ErrorResponse(HttpStatus.BAD_REQUEST, "Ошибка валидации", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException exception) {
        log.warn(exception.getMessage(), exception);
        return new ErrorResponse(HttpStatus.NOT_FOUND, "Не найдена сущность", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleConstraintValidationException(final ConstraintViolationException exception) {
        log.warn(exception.getMessage(), exception);

        final List<Violation> violations = exception.getConstraintViolations().stream()
                .map(violation -> new Violation(violation.getPropertyPath().toString(), violation.getMessage()))
                .toList();

        return new ValidationErrorResponse(HttpStatus.BAD_REQUEST, violations);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleMethodArgumentNotValid(final MethodArgumentNotValidException exception) {
        log.warn(exception.getMessage(), exception);

        final List<Violation> violations = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .toList();

        return new ValidationErrorResponse(HttpStatus.BAD_REQUEST, violations);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOther(final Throwable exception) {
        log.warn(exception.getMessage(), exception);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Неизвестная ошибка", exception.getMessage());
    }
}