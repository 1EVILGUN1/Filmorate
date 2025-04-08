package ru.yandex.practicum.filmorate.rating.repository;

import ru.yandex.practicum.filmorate.rating.model.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingRepository {
    Optional<Rating> get(Long id);

    List<Rating> getAll();
}
