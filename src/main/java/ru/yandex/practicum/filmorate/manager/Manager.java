package ru.yandex.practicum.filmorate.manager;


import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Manager {
    private static final Logger logFilm = LoggerFactory.getLogger(Film.class);
    private static final Logger logUser = LoggerFactory.getLogger(User.class);
    protected int counterId = 0;
    protected final Map<Integer, User> users = new HashMap<>();
    protected final Map<Integer, Film> films = new HashMap<>();

    public User createUser(@NotNull User user) {
        try {
            final int id = ++counterId;
            user.setId(id);
            checkUser(user);
            users.put(id, user);
            logUser.info("User created: {}", user);
            return user;
        } catch (ValidationException e) {
            logUser.warn("User creation failed: {}", e.getMessage());
            return null;
        }
    }

    public Film createFilm(@NotNull Film film) {
        try {
            final int id = ++counterId;
            film.setId(id);
            checkFilm(film);
            films.put(id, film);
            logFilm.info("Film created: {}", film);
            return film;
        } catch (ValidationException e) {
            logFilm.warn("Film creation failed {}", e.getMessage());
            return null;
        }
    }

    public User updateUser(User user) {
        try {
            checkUser(user);
            User updatedUser = users.get(user.getId());
            if (updatedUser != null) {
                users.put(updatedUser.getId(), user);
                logUser.info("User updated: {}", user);
                return updatedUser;
            } else return null;
        } catch (ValidationException e) {
            logUser.warn("User update failed: {}", e.getMessage());
            return null;
        }
    }

    public Film updateFilm(Film film) {
        try {
            checkFilm(film);
            Film updatedFilm = films.get(film.getId());
            if (updatedFilm != null) {
                films.put(film.getId(), film);
                logFilm.info("Film updated: {}", film);
                return film;
            } else return null;
        } catch (ValidationException e) {
            logFilm.warn("Film update failed: {}", e.getMessage());
            return null;
        }
    }

    public List<User> getUsers() {
        logUser.info("getUsers");
        if (users.isEmpty()) return null;
        return new ArrayList<>(users.values());
    }

    public List<Film> getFilms() {
        logUser.info("getFilms");
        if (films.isEmpty()) return null;
        return new ArrayList<>(films.values());
    }

    public User getUser(int id) {
        logUser.info("getUser {}", id);
        return users.get(id);
    }

    public Film getFilm(int id) {
        logUser.info("getFilm {}", id);
        return films.get(id);
    }

    private void checkFilm(Film film) throws ValidationException {
        if (film.getName().isBlank())
            throw new ValidationException("Название пустое");
        if (film.getDescription().length() > 200)
            throw new ValidationException("Описание фильма больше " + 200 + " символов");
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28")))
            throw new ValidationException("Дата релиза фильма раньше " + LocalDate.parse("1895-12-28"));
        if (film.getDuration() < 1)
            throw new ValidationException("Продолжительность фильма должна быть больше или равна 1 минуте");
    }

    private void checkUser(User user) throws ValidationException {
        if (!user.getEmail().contains("@") || user.getEmail().isBlank())
            throw new ValidationException("Не верный email");
        if (!(user.getLogin().replaceAll(" ", "").equals(user.getLogin())))
            throw new ValidationException("Не правильный логин");
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("День рождения указана в будущем времени");
    }
}