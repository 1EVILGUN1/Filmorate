package filmorate.film.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import filmorate.activity.model.Activity;
import filmorate.activity.repository.ActivityRepository;
import filmorate.director.model.Director;
import filmorate.director.repository.DirectorRepository;
import filmorate.film.dto.FilmDto;
import filmorate.film.dto.FilmRequest;
import filmorate.film.model.Film;
import filmorate.film.repository.FilmRepository;
import filmorate.genre.dto.GenreRequest;
import filmorate.director.dto.DirectorRequest;
import filmorate.exception.ElementNotExistsException;
import filmorate.exception.NotFoundException;
import filmorate.exception.ValidationException;
import filmorate.film.mapper.FilmMapper;
import filmorate.genre.model.Genre;
import filmorate.genre.repository.GenreRepository;
import filmorate.enums.EventType;
import filmorate.enums.Operation;
import filmorate.rating.model.Rating;
import filmorate.rating.repository.RatingRepository;
import filmorate.user.repository.UserRepository;
import filmorate.util.Util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {
    private static final String FILM_NOT_FOUND_MSG = "Фильм с ID: {} не найден";
    private static final String INVALID_ID_MSG = "Должен быть указан ID";

    private final FilmRepository repository;
    private final RatingRepository ratingRepository;
    private final GenreRepository genreRepository;
    private final UserRepository userRepository;
    private final DirectorRepository directorRepository;
    private final ActivityRepository activityRepository;

    public FilmDto get(Long id) {
        log.info("Получение фильма с ID: {}", id);
        return executeWithLogging(
                () -> repository.findById(id)
                        .map(FilmMapper::mapToFilmDto)
                        .orElseThrow(() -> new NotFoundException(formatNotFoundMessage(id))),
                "Найден фильм: {}",
                FILM_NOT_FOUND_MSG,
                "Ошибка при получении фильма с ID: {}", id
        );
    }

    public List<FilmDto> getAll() {
        log.info("Получение всех фильмов");
        return executeWithLogging(
                () -> repository.getAll().stream()
                        .map(FilmMapper::mapToFilmDto)
                        .collect(Collectors.toList()),
                "Получено {} фильмов",
                null,
                "Ошибка при получении всех фильмов"
        );
    }

    public FilmDto save(FilmRequest request) {
        log.info("Создание нового фильма: {}", request);
        try {
            Film film = FilmMapper.mapToFilm(request);
            enrichFilmData(film, request);
            Film savedFilm = repository.save(film);
            log.debug("Фильм сохранен с ID: {}", savedFilm.getId());
            return FilmMapper.mapToFilmDto(savedFilm);
        } catch (Exception e) {
            log.error("Ошибка при создании фильма: {}", request, e);
            throw e;
        }
    }

    public FilmDto update(FilmRequest request) {
        log.info("Обновление фильма с ID: {}", request.getId());
        try {
            validateFilmId(request.getId());
            Film updatedFilm = updateFilmFields(request);
            enrichFilmData(updatedFilm, request);
            Film savedFilm = repository.update(updatedFilm);
            log.debug("Обновлен фильм: {}", savedFilm);
            return FilmMapper.mapToFilmDto(savedFilm);
        } catch (NotFoundException e) {
            log.warn(FILM_NOT_FOUND_MSG, request.getId());
            throw e;
        } catch (ValidationException e) {
            log.warn("Ошибка валидации при обновлении фильма: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Ошибка при обновлении фильма: {}", request, e);
            throw e;
        }
    }

    public boolean delete(Long id) {
        log.info("Удаление фильма с ID: {}", id);
        return executeWithLogging(
                () -> repository.delete(id),
                "Результат удаления фильма с ID: {} - {}",
                null,
                "Ошибка при удалении фильма с ID: {}", id
        );
    }

    public boolean putLike(Long id, Long userId) {
        log.info("Добавление лайка фильму {} от пользователя {}", id, userId);
        try {
            validateIds(id, userId);
            if (repository.findLike(id, userId)) {
                log.warn("Пользователь {} уже ставил лайк фильму {}", userId, id);
                return false;
            }
            recordActivity(userId, EventType.LIKE, Operation.ADD, id);
            boolean result = repository.putLike(id, userId);
            log.debug("Результат добавления лайка: {}", result);
            return result;
        } catch (NotFoundException e) {
            log.warn("Ошибка валидации ID при добавлении лайка: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Ошибка при добавлении лайка фильму {} от пользователя {}", id, userId, e);
            throw e;
        }
    }

    public boolean deleteLike(Long id, Long userId) {
        log.info("Удаление лайка фильму {} от пользователя {}", id, userId);
        try {
            validateIds(id, userId);
            recordActivity(userId, EventType.LIKE, Operation.REMOVE, id);
            boolean result = repository.deleteLike(id, userId);
            log.debug("Результат удаления лайка: {}", result);
            return result;
        } catch (NotFoundException e) {
            log.warn("Ошибка валидации ID при удалении лайка: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Ошибка при удалении лайка фильму {} от пользователя {}", id, userId, e);
            throw e;
        }
    }

    public List<FilmDto> getTopFilms(int count, Long genreId, Integer year) {
        log.info("Получение топ-{} фильмов, genreId: {}, year: {}", count, genreId, year);
        return executeWithLogging(
                () -> {
                    if (genreId != null) Util.checkId(genreRepository, genreId);
                    return repository.getTopFilms(count, genreId, year).stream()
                            .map(FilmMapper::mapToFilmDto)
                            .collect(Collectors.toList());
                },
                "Найдено {} фильмов в топе",
                "Жанр с ID: {} не найден",
                "Ошибка при получении топ-{} фильмов, genreId: {}, year: {}", count, genreId, year
        );
    }

    public List<FilmDto> getCommonFilms(Long userId, Long friendId) {
        log.info("Поиск общих фильмов пользователей {} и {}", userId, friendId);
        return executeWithLogging(
                () -> {
                    Util.checkId(userRepository, userId, friendId);
                    return repository.findCommonFilms(userId, friendId).stream()
                            .map(FilmMapper::mapToFilmDto)
                            .collect(Collectors.toList());
                },
                "Найдено {} общих фильмов",
                "Пользователь с ID: {} или {} не найден",
                "Ошибка при поиске общих фильмов пользователей {} и {}", userId, friendId
        );
    }

    public List<FilmDto> getDirectorsFilmsByYear(Long id) {
        log.info("Получение фильмов режиссера {} отсортированных по году", id);
        return executeWithLogging(
                () -> {
                    Util.checkId(directorRepository, id);
                    return repository.getDirectorsFilmSortByYear(id).stream()
                            .map(FilmMapper::mapToFilmDto)
                            .collect(Collectors.toList());
                },
                "Найдено {} фильмов режиссера, отсортированных по году",
                "Режиссер с ID: {} не найден",
                "Ошибка при получении фильмов режиссера {} отсортированных по году", id
        );
    }

    public List<FilmDto> getDirectorsFilmsByLikes(Long id) {
        log.info("Получение фильмов режиссера {} отсортированных по лайкам", id);
        return executeWithLogging(
                () -> {
                    Util.checkId(directorRepository, id);
                    return repository.getDirectorsFilmSortByLikes(id).stream()
                            .map(FilmMapper::mapToFilmDto)
                            .collect(Collectors.toList());
                },
                "Найдено {} фильмов режиссера, отсортированных по лайкам",
                "Режиссер с ID: {} не найден",
                "Ошибка при получении фильмов режиссера {} отсортированных по лайкам", id
        );
    }

    public List<FilmDto> getSearchFilm(String query) {
        log.info("Поиск фильмов по названию: {}", query);
        return executeWithLogging(
                () -> repository.getSearchFilm(query.toLowerCase()).stream()
                        .map(FilmMapper::mapToFilmDto)
                        .collect(Collectors.toList()),
                "Найдено {} фильмов по названию",
                null,
                "Ошибка при поиске фильмов по названию: {}", query
        );
    }

    public List<FilmDto> getSearchDirector(String query) {
        log.info("Поиск фильмов по режиссеру: {}", query);
        return executeWithLogging(
                () -> repository.getSearchDirector(query.toLowerCase()).stream()
                        .map(FilmMapper::mapToFilmDto)
                        .collect(Collectors.toList()),
                "Найдено {} фильмов по режиссеру",
                null,
                "Ошибка при поиске фильмов по режиссеру: {}", query
        );
    }

    // Вспомогательные методы
    private <T> T executeWithLogging(SupplierWithException<T> supplier, String successMsg, String warnMsg, String errorMsg, Object... args) {
        try {
            T result = supplier.get();
            log.debug(successMsg, getLogArgs(result, args));
            return result;
        } catch (NotFoundException | ElementNotExistsException e) {
            if (warnMsg != null) log.warn(warnMsg, args);
            throw e;
        } catch (Exception e) {
            log.error(errorMsg, args, e);
            throw new RuntimeException(e);
        }
    }

    private void validateFilmId(Long id) {
        if (id == null) {
            log.error("Попытка обновления фильма без указания ID");
            throw new ValidationException("ID", INVALID_ID_MSG);
        }
    }

    private void validateIds(Long filmId, Long userId) {
        Util.checkId(repository, filmId);
        Util.checkId(userRepository, userId);
    }

    private Film updateFilmFields(FilmRequest request) {
        Film film = repository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException(formatNotFoundMessage(request.getId())));
        log.debug("Найден фильм для обновления: {}", film);
        return FilmMapper.updateFilmFields(film, request);
    }

    private void enrichFilmData(Film film, FilmRequest request) {
        addRating(film, request);
        addGenres(film, request);
        addDirector(film, request);
    }

    private void addRating(Film film, FilmRequest request) {
        Long ratingId = request.getMpa().getId();
        log.debug("Добавление рейтинга MPA с ID: {} для фильма {}", ratingId, film.getId());
        Rating mpa = ratingRepository.get(ratingId)
                .orElseThrow(() -> new ValidationException("ID", "Рейтинг с ID " + ratingId + " не найден"));
        film.setMpa(mpa);
    }

    private void addGenres(Film film, FilmRequest request) {
        log.debug("Обновление жанров для фильма {}", film.getId());
        repository.deleteAllGenresForFilm(film.getId());
        Set<Genre> genres = fetchAndLinkGenres(film.getId(), request.getGenres());
        film.setGenres(genres);
        log.debug("Добавлено {} жанров для фильма {}", genres.size(), film.getId());
    }

    private void addDirector(Film film, FilmRequest request) {
        log.debug("Обновление режиссеров для фильма {}", film.getId());
        repository.deleteAllDirectorsForFilm(film.getId());
        Set<Director> directors = fetchAndLinkDirectors(film.getId(), request.getDirectors());
        film.setDirectors(directors);
        log.debug("Добавлено {} режиссеров для фильма {}", directors.size(), film.getId());
    }

    private Set<Genre> fetchAndLinkGenres(Long filmId, List<GenreRequest> genreRequests) {
        if (genreRequests == null || genreRequests.isEmpty()) {
            log.debug("Жанры не указаны, установлен пустой список");
            return new HashSet<>();
        }
        return genreRequests.stream()
                .map(GenreRequest::getId)
                .peek(id -> log.trace("Обработка жанра с ID: {}", id))
                .map(id -> genreRepository.findById(id)
                        .orElseThrow(() -> new ElementNotExistsException("Жанр не найден")))
                .peek(genre -> repository.addGenreForFilm(filmId, genre.getId()))
                .collect(Collectors.toSet());
    }

    private Set<Director> fetchAndLinkDirectors(Long filmId, List<DirectorRequest> directorRequests) {
        if (directorRequests == null || directorRequests.isEmpty()) {
            log.debug("Режиссеры не указаны, установлен пустой список");
            return new HashSet<>();
        }
        return directorRequests.stream()
                .map(DirectorRequest::getId)
                .peek(id -> log.trace("Обработка режиссера с ID: {}", id))
                .map(id -> directorRepository.findById(id)
                        .orElseThrow(() -> new ElementNotExistsException("Режиссер не найден")))
                .peek(director -> repository.addDirectorForFilm(filmId, director.getId()))
                .collect(Collectors.toSet());
    }

    private void recordActivity(Long userId, EventType eventType, Operation operation, Long entityId) {
        Activity activity = new Activity(userId, eventType, operation, entityId);
        activityRepository.save(activity);
        log.debug("Создано событие активности: {}", activity);
    }

    private String formatNotFoundMessage(Long id) {
        return "Фильм с ID = " + id + " не найден";
    }

    private Object[] getLogArgs(Object result, Object... args) {
        if (result instanceof List) return new Object[]{((List<?>) result).size(), args};
        if (result instanceof Boolean) return new Object[]{args[0], result};
        return new Object[]{result};
    }

    @FunctionalInterface
    private interface SupplierWithException<T> {
        T get() throws Exception;
    }
}