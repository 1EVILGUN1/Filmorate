package ru.yandex.practicum.filmorate.genre.service;

import ru.yandex.practicum.filmorate.genre.dto.GenreDto;


import java.util.List;

public interface GenreService {
    GenreDto get(Long id);

    List<GenreDto> getAll();
}
