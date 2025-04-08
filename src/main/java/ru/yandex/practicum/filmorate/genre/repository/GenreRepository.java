package ru.yandex.practicum.filmorate.genre.repository;

import ru.yandex.practicum.filmorate.genre.model.Genre;
import ru.yandex.practicum.filmorate.pubRepository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository extends Repository {
    Optional<Genre> findById(Long id);

    List<Genre> getAll();

    Set<Genre> getForFilm(Long id);
}
