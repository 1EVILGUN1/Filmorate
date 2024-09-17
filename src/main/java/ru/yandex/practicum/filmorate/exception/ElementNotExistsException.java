package ru.yandex.practicum.filmorate.exception;

public class ElementNotExistsException extends RuntimeException {
    public ElementNotExistsException(String message) {
        super(message);
    }
}
