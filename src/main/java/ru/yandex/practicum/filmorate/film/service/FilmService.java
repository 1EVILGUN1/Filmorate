package ru.yandex.practicum.filmorate.film.service;

import ru.yandex.practicum.filmorate.film.dto.FilmDto;
import ru.yandex.practicum.filmorate.film.dto.FilmRequest;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.pubService.BaseService;

import java.util.*;

public interface FilmService extends BaseService<FilmDto, FilmRequest> {

    FilmDto get(Long id);

    List<FilmDto> getAll();

    FilmDto save(FilmRequest request);

    FilmDto update(FilmRequest request);

    boolean putLike(Long id, Long userId);

    boolean deleteLike(Long id, Long userId);

    List<FilmDto> getCommonFilms(Long userId, Long friendId);

    List<FilmDto> getTopFilms(int count, Long genreId, Integer year);

    List<FilmDto> getDirectorsFilmsByYear(Long id);

    List<FilmDto> getDirectorsFilmsByLikes(Long id);

    List<FilmDto> getSearchFilm(String query);

    List<FilmDto> getSearchDirector(String query);
}