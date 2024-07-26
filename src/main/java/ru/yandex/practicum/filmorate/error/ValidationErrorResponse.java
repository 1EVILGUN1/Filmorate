package ru.yandex.practicum.filmorate.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ValidationErrorResponse {
    private final HttpStatus status;
    private final List<Violation> violations;
}