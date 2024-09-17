package ru.yandex.practicum.filmorate.controller.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.controller.model.rating.RatingDto;
import ru.yandex.practicum.filmorate.model.Rating;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RatingMapper {
    public static RatingDto mapToMpaDto(Rating mpa) {
        RatingDto mpaDto = new RatingDto();
        mpaDto.setId(mpa.getId());
        mpaDto.setName(mpa.getName());
        return mpaDto;
    }

    public static Rating mapToMpa(RatingDto mpaDto) {
        Rating mpa = new Rating();
        mpa.setId(mpaDto.getId());
        mpa.setName(mpaDto.getName());
        return mpa;
    }
}
