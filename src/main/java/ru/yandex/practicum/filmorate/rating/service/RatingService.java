package ru.yandex.practicum.filmorate.rating.service;

import ru.yandex.practicum.filmorate.rating.dto.RatingDto;
import java.util.List;

public interface RatingService {
    RatingDto get(Long id);

    List<RatingDto> getAll();
}
