package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.controller.model.film.FilmDto;
import ru.yandex.practicum.filmorate.controller.model.film.FilmRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
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