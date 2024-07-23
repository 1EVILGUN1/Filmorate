package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final Logger logFilm = LoggerFactory.getLogger(Film.class);
    private int counterId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        final int id = ++counterId;
        film.setId(id);
        checkFilm(film);
        films.put(id, film);
        logFilm.info("GET Запрос на создание фильма выполнен: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        checkFilm(film);
        Film updatedFilm = films.get(film.getId());
        if (updatedFilm != null) {
            films.put(film.getId(), film);
            logFilm.info("PUT Запрос на изменение фильма выполнен: {}", film);
            return film;
        } else {
            return null;
        }
    }

    @Override
    public List<Film> getFilms() {
        logFilm.info("GET Запрос выполняется на получение списка фильмов");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(int id) {
        logFilm.info("GET Запрос выполняется на получение фильма по id {}", id);
        return films.get(id);
    }

    private void checkFilm(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            throw new ValidationException("Название пустое");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма больше " + 200 + " символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            throw new ValidationException("Дата релиза фильма раньше " + LocalDate.parse("1895-12-28"));
        }
        if (film.getDuration() < 1) {
            throw new ValidationException("Продолжительность фильма должна быть больше или равна 1 минуте");
        }
    }
}
