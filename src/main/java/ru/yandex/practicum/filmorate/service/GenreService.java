package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.controller.model.genre.GenreDto;


import java.util.List;

public interface GenreService {
    GenreDto get(Long id);

    List<GenreDto> getAll();
}
