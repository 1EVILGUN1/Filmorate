package ru.yandex.practicum.filmorate.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class ErrorResponse {
    private final HttpStatus status;
    private final String error;
    private final String description;
}