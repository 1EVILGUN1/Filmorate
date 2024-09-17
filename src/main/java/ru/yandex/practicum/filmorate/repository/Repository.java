package ru.yandex.practicum.filmorate.repository;

import java.util.Optional;

public interface Repository {
    Optional<?> findById(Long id);
}
