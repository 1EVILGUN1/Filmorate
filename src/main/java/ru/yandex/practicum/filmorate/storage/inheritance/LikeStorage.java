package ru.yandex.practicum.filmorate.storage.inheritance;

import ru.yandex.practicum.filmorate.model.Film;

public interface LikeStorage {
    void getLikesOfFilm(Film film);

}
