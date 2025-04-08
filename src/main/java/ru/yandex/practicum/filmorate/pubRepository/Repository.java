package ru.yandex.practicum.filmorate.pubRepository;

import java.util.Optional;

public interface Repository {
    Optional<?> findById(Long id);
}
