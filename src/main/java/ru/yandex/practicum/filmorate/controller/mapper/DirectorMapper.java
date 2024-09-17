package ru.yandex.practicum.filmorate.controller.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.controller.model.director.DirectorDto;
import ru.yandex.practicum.filmorate.controller.model.director.DirectorRequest;
import ru.yandex.practicum.filmorate.model.Director;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DirectorMapper {
    public static DirectorDto mapToDirectorDto(Director director) {
        DirectorDto directorDto = new DirectorDto();
        directorDto.setId(director.getId());
        directorDto.setName(director.getName());
        return directorDto;
    }

    public static Director mapToDirector(DirectorRequest request) {
        Director director = new Director();
        director.setName(request.getName());
        return director;
    }

    public static Director updateDirectorFields(Director director, DirectorRequest request) {
        director.setName(request.getName());
        return director;
    }
}
