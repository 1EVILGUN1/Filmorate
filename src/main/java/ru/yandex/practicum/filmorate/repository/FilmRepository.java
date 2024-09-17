package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository extends Repository {
    Optional<Film> findById(Long id);

    List<Film> getAll();

    Film save(Film film);

    Film update(Film newFilm);

    boolean delete(Long id);

    boolean findLike(Long id, Long userId);

    boolean putLike(Long id, Long userId);

    boolean deleteLike(Long id, Long userId);

    List<Film> getTopFilms(int count, Long genreId, Integer year);

    void addGenreForFilm(Long id, Long genreId);

    void deleteAllGenresForFilm(Long id);

    void addDirectorForFilm(Long id, Long directorId);

    List<Film> getDirectorsFilmSortByYear(Long id);

    List<Film> getDirectorsFilmSortByLikes(Long id);

    void deleteAllDirectorsForFilm(Long id);

    List<Film> findCommonFilms(Long userId, Long friendId);

    List<Film> getSearchFilm(String query);

    List<Film> getSearchDirector(String query);

    List<Film> getRecommendations(long userId, long bestRepetitionUserId);
}
