package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.model.film.FilmDto;
import ru.yandex.practicum.filmorate.controller.model.film.FilmRequest;
import ru.yandex.practicum.filmorate.controller.model.genre.GenreRequest;
import ru.yandex.practicum.filmorate.controller.model.director.DirectorRequest;
import ru.yandex.practicum.filmorate.exception.ElementNotExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.controller.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;
import ru.yandex.practicum.filmorate.repository.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.util.Util;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final RatingRepository ratingRepository;
    private final GenreRepository genreRepository;
    private final UserRepository userRepository;
    private final DirectorRepository directorRepository;
    private final ActivityRepository activityRepository;

    public FilmDto get(Long id) {
        return filmRepository.findById(id)
                .map(FilmMapper::mapToFilmDto)
                .orElseThrow(() -> new NotFoundException("Фильм с ID = " + id + " не найден"));
    }

    public List<FilmDto> getAll() {
        return filmRepository.getAll().stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmDto save(FilmRequest request) {
        Film film = FilmMapper.mapToFilm(request);
        addRating(film, request);
        film = filmRepository.save(film);
        addGenres(film, request);
        addDirector(film, request);
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto update(FilmRequest request) {
        if (request.getId() == null) {
            throw new ValidationException("ID", "Должен быть указан ID");
        }

        Film updatedFilm = filmRepository.findById(request.getId())
                .map(film -> FilmMapper.updateFilmFields(film, request))
                .orElseThrow(() -> new NotFoundException("Фильм с ID " + request.getId() + " не найден"));
        addRating(updatedFilm, request);
        addGenres(updatedFilm, request);
        addDirector(updatedFilm, request);
        updatedFilm = filmRepository.update(updatedFilm);
        return FilmMapper.mapToFilmDto(updatedFilm);
    }

    public boolean delete(Long id) {
        return filmRepository.delete(id);
    }

    public boolean putLike(Long id, Long userId) {
        Util.checkId(filmRepository, id);
        Util.checkId(userRepository, userId);

        Activity activity = new Activity(userId, EventType.LIKE, Operation.ADD, id);
        activityRepository.save(activity);

        if (filmRepository.findLike(id, userId)) {
            return false;
        }



        return filmRepository.putLike(id, userId);
    }

    public boolean deleteLike(Long id, Long userId) {
        Util.checkId(filmRepository, id);
        Util.checkId(userRepository, userId);

        Activity activity = new Activity(userId, EventType.LIKE, Operation.REMOVE, id);
        activityRepository.save(activity);

        return filmRepository.deleteLike(id, userId);
    }

    public List<FilmDto> getTopFilms(int count, Long genreId, Integer year) {
        if (genreId != null) {
            Util.checkId(genreRepository, genreId);
        }
        return filmRepository.getTopFilms(count, genreId, year).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public List<FilmDto> getCommonFilms(Long userId, Long friendId) {
        Util.checkId(userRepository, userId);
        Util.checkId(userRepository, friendId);
        return filmRepository.findCommonFilms(userId, friendId).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public List<FilmDto> getDirectorsFilmsByYear(Long id) {
        Util.checkId(directorRepository, id);
        return filmRepository.getDirectorsFilmSortByYear(id).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public List<FilmDto> getDirectorsFilmsByLikes(Long id) {
        Util.checkId(directorRepository, id);
        return filmRepository.getDirectorsFilmSortByLikes(id).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }


    public List<FilmDto> getSearchFilm(String query) {
        return filmRepository.getSearchFilm(query.toLowerCase()).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public List<FilmDto> getSearchDirector(String query) {
        return filmRepository.getSearchDirector(query.toLowerCase()).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }


    private void addRating(Film film, FilmRequest request) {
        Long ratingId = request.getMpa().getId();
        Rating mpa = ratingRepository.get(ratingId)
                .orElseThrow(() -> new ValidationException("ID", "Рейтинг с ID " + ratingId + " не найден"));
        film.setMpa(mpa);
    }

    private void addGenres(Film film, FilmRequest request) {
        filmRepository.deleteAllGenresForFilm(film.getId());
        if (request.getGenres() == null) {
            film.setGenres(new HashSet<>());
            return;
        }

        Set<Genre> genres = request.getGenres().stream()
                .map(GenreRequest::getId)
                .map(genreRepository::findById)
                .map(genre -> genre
                        .orElseThrow(() -> new ElementNotExistsException("Жанр не найден")))
                .peek(genre -> filmRepository.addGenreForFilm(film.getId(), genre.getId()))
                .collect(Collectors.toSet());
        film.setGenres(genres);
    }

    private void addDirector(Film film, FilmRequest request) {
        filmRepository.deleteAllDirectorsForFilm(film.getId());
        if (request.getDirectors() == null) {
            film.setDirectors(new HashSet<>());
            return;
        }

        Set<Director> directors = request.getDirectors().stream()
                .map(DirectorRequest::getId)
                .map(directorRepository::findById)
                .map(director -> director
                        .orElseThrow(() -> new ElementNotExistsException("Режиссер не найден")))
                .peek(director -> filmRepository.addDirectorForFilm(film.getId(), director.getId()))
                .collect(Collectors.toSet());
        film.setDirectors(directors);
    }
}
