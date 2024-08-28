package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.inheritance.GenreStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Collection<Genre> getAllGenres() {
        return Collections.unmodifiableCollection(genreStorage.getAllGenres().values());
    }

    public Genre getGenreById(int id) {
        return genreStorage.findGenreById(id).orElseThrow(GenreDoesNotExistException::new);
    }

    public void getGenresOfFilm(Film film) {
        List<Genre> genres = genreStorage.getGenresOfFilm(film.getId());
        for (Genre genre : genres) {
            film.getGenres().add(genre);
        }
    }
}
