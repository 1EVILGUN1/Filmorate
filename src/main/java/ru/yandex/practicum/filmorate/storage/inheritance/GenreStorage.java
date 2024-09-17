package ru.yandex.practicum.filmorate.storage.inheritance;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GenreStorage {
    Map<Integer, Genre> getAllGenres();

    Optional<Genre> findGenreById(Integer id);

    List<Genre> getGenresOfFilm(long filmId);
}
