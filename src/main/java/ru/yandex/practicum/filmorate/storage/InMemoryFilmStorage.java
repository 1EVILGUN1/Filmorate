package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private long counterId = 0;
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        final long id = ++counterId;
        film.setId(id);
        films.put(id, film);
        log.info("GET Запрос на создание фильма выполнен: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        @NotNull
        Long id = film.getId();
        Film oldFilm = films.get(film.getId());
        if (oldFilm == null) {
            throw new NotFoundException("PUT Запрос. Фильм с ID = " + id + " не найден");
        } else {
            films.put(film.getId(), film);
            log.info("PUT Запрос на изменение фильма выполнен: {}", film);
            return film;
        }
    }

    @Override
    public List<Film> getFilms() {
        log.info("GET Запрос выполняется на получение списка фильмов");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(long id) {
        log.info("GET Запрос выполняется на получение фильма по id {}", id);
        if (films.get(id) == null) {
            throw new NotFoundException("Фильм с ID = " + id + " не найден");
        } else {
            return films.get(id);
        }
    }
}
