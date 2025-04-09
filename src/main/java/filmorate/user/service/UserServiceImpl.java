package filmorate.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import filmorate.activity.mapper.ActivityMapper;
import filmorate.film.mapper.FilmMapper;
import filmorate.genre.mapper.GenreMapper;
import filmorate.activity.dto.ActivityDto;
import filmorate.film.dto.FilmDto;
import filmorate.user.dto.UserRequest;
import filmorate.user.dto.UserDto;
import filmorate.exception.NotFoundException;
import filmorate.exception.ValidationException;
import filmorate.user.mapper.UserMapper;
import filmorate.activity.model.Activity;
import filmorate.user.model.User;
import filmorate.enums.EventType;
import filmorate.enums.Operation;
import filmorate.activity.repository.ActivityRepository;
import filmorate.film.repository.FilmRepository;
import filmorate.genre.repository.GenreRepository;
import filmorate.user.repository.UserRepository;
import filmorate.util.Util;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private static final String USER_NOT_FOUND_MSG = "Пользователь с ID: {} не найден";
    private static final String SELF_FRIEND_ERROR_MSG = "Нельзя добавить/удалить самого себя в друзья: ID: {}";

    private final UserRepository repository;
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;
    private final ActivityRepository activityRepository;

    public UserDto get(Long id) {
        log.info("Запрос на получение пользователя с ID: {}", id);
        return executeWithLogging(
                () -> repository.findById(id)
                        .map(UserMapper::mapToUserDto)
                        .orElseThrow(() -> new NotFoundException(formatNotFoundMessage(id))),
                "Успешно получен пользователь: {}",
                USER_NOT_FOUND_MSG,
                "Ошибка при получении пользователя с ID: {}", id
        );
    }

    public List<UserDto> getAll() {
        log.info("Запрос на получение всех пользователей");
        return executeWithLogging(
                () -> repository.getAll().stream()
                        .map(UserMapper::mapToUserDto)
                        .collect(Collectors.toList()),
                "Успешно получено {} пользователей",
                null,
                "Ошибка при получении списка пользователей"
        );
    }

    public UserDto save(UserRequest request) {
        log.info("Запрос на создание пользователя: {}", request);
        try {
            UserRequest adjustedRequest = adjustUserName(request);
            User user = UserMapper.mapToUser(adjustedRequest);
            User savedUser = repository.save(user);
            log.debug("Пользователь успешно создан с ID: {}", savedUser.getId());
            return UserMapper.mapToUserDto(savedUser);
        } catch (Exception e) {
            log.error("Ошибка при создании пользователя: {}", request, e);
            throw e;
        }
    }

    public UserDto update(UserRequest request) {
        log.info("Запрос на обновление пользователя: {}", request);
        try {
            validateUserId(request.getId());
            User updatedUser = repository.findById(request.getId())
                    .map(user -> UserMapper.updateUserFields(user, request))
                    .orElseThrow(() -> new NotFoundException(formatNotFoundMessage(request.getId())));
            User savedUser = repository.update(updatedUser);
            log.debug("Пользователь успешно обновлен с ID: {}", savedUser.getId());
            return UserMapper.mapToUserDto(savedUser);
        } catch (NotFoundException e) {
            log.warn(USER_NOT_FOUND_MSG, request.getId());
            throw e;
        } catch (ValidationException e) {
            log.warn("Ошибка валидации при обновлении пользователя: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Ошибка при обновлении пользователя: {}", request, e);
            throw e;
        }
    }

    public boolean delete(Long id) {
        log.info("Запрос на удаление пользователя с ID: {}", id);
        return executeWithLogging(
                () -> repository.delete(id),
                "Результат удаления пользователя с ID: {} - {}",
                null,
                "Ошибка при удалении пользователя с ID: {}", id
        );
    }

    public List<UserDto> getFriends(Long id) {
        log.info("Запрос на получение друзей пользователя с ID: {}", id);
        return executeWithLogging(
                () -> {
                    Util.checkId(repository, id);
                    return repository.getFriends(id).stream()
                            .map(UserMapper::mapToUserDto)
                            .sorted(Comparator.comparingLong(UserDto::getId))
                            .collect(Collectors.toList());
                },
                "Успешно получено {} друзей для пользователя ID: {}",
                USER_NOT_FOUND_MSG,
                "Ошибка при получении друзей пользователя с ID: {}", id
        );
    }

    public Set<UserDto> getCommonFriends(Long id, Long otherId) {
        log.info("Запрос на получение общих друзей пользователей ID: {} и ID: {}", id, otherId);
        return executeWithLogging(
                () -> {
                    Util.checkId(repository, id, otherId);
                    return repository.getCommonFriends(id, otherId).stream()
                            .map(UserMapper::mapToUserDto)
                            .collect(Collectors.toSet());
                },
                "Успешно получено {} общих друзей для пользователей ID: {} и ID: {}",
                "Один из пользователей с ID: {} или ID: {} не найден для получения общих друзей",
                "Ошибка при получении общих друзей пользователей ID: {} и ID: {}", id, otherId
        );
    }

    public boolean addFriend(Long id, Long otherId) {
        log.info("Запрос на добавление друга ID: {} пользователю ID: {}", otherId, id);
        try {
            checkSelfFriendship(id, otherId);
            Util.checkId(repository, id, otherId);
            return handleFriendshipOperation(id, otherId, true);
        } catch (NotFoundException e) {
            log.warn("Один из пользователей с ID: {} или ID: {} не найден для добавления в друзья", id, otherId);
            throw e;
        } catch (RuntimeException e) {
            log.warn("Ошибка валидации при добавлении друга: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Ошибка при добавлении друга ID: {} пользователю ID: {}", otherId, id, e);
            throw e;
        }
    }

    public boolean deleteFriend(Long id, Long otherId) {
        log.info("Запрос на удаление друга ID: {} у пользователя ID: {}", otherId, id);
        try {
            checkSelfFriendship(id, otherId);
            Util.checkId(repository, id, otherId);
            return handleFriendshipOperation(id, otherId, false);
        } catch (NotFoundException e) {
            log.warn("Один из пользователей с ID: {} или ID: {} не найден для удаления из друзей", id, otherId);
            throw e;
        } catch (RuntimeException e) {
            log.warn("Ошибка валидации при удалении друга: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Ошибка при удалении друга ID: {} у пользователя ID: {}", otherId, id, e);
            throw e;
        }
    }

    public List<FilmDto> getRecommendations(Long userId) {
        log.info("Запрос на получение рекомендаций для пользователя ID: {}", userId);
        try {
            List<Long> bestRepetitionUserIds = repository.getBestRepetitionUserIds(userId);
            if (bestRepetitionUserIds.isEmpty()) {
                log.debug("Рекомендации для пользователя ID: {} отсутствуют, возвращен пустой список", userId);
                return new ArrayList<>();
            }
            return fetchAndEnrichRecommendations(userId, bestRepetitionUserIds);
        } catch (Exception e) {
            log.error("Ошибка при получении рекомендаций для пользователя ID: {}", userId, e);
            throw e;
        }
    }

    @Override
    public List<ActivityDto> getUserFeed(Long id) {
        log.info("Запрос на получение ленты активности пользователя ID: {}", id);
        return executeWithLogging(
                () -> {
                    Util.checkId(repository, id);
                    return activityRepository.getUserFeed(id)
                            .stream()
                            .map(ActivityMapper::mapToActivityDto)
                            .collect(Collectors.toList());
                },
                "Успешно получено {} событий в ленте активности пользователя ID: {}",
                USER_NOT_FOUND_MSG,
                "Ошибка при получении ленты активности пользователя ID: {}", id
        );
    }

    // Вспомогательные методы
    private <T> T executeWithLogging(SupplierWithException<T> supplier, String successMsg, String warnMsg, String errorMsg, Object... args) {
        try {
            T result = supplier.get();
            log.debug(successMsg, getLogArgs(result, args));
            return result;
        } catch (NotFoundException e) {
            if (warnMsg != null) log.warn(warnMsg, args);
            throw e;
        } catch (Exception e) {
            log.error(errorMsg, args, e);
            throw new RuntimeException(e);
        }
    }

    private UserRequest adjustUserName(UserRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            log.debug("Имя пользователя пустое, используется логин: {}", request.getLogin());
            request.setName(request.getLogin());
        }
        return request;
    }

    private void validateUserId(Long id) {
        if (id == null) {
            log.warn("Отсутствует ID в запросе на обновление пользователя");
            throw new ValidationException("ID", "Должен быть указан ID");
        }
    }

    private void checkSelfFriendship(Long id, Long otherId) {
        if (id.equals(otherId)) {
            log.warn(SELF_FRIEND_ERROR_MSG, id);
            throw new RuntimeException("Нельзя добавить самого себя в друзья");
        }
    }

    private boolean handleFriendshipOperation(Long id, Long otherId, boolean isAdd) {
        boolean result;
        if (isAdd && repository.isFriendRequest(id, otherId)) {
            result = repository.acceptRequest(id, otherId);
            log.debug("Заявка в друзья от ID: {} к ID: {} принята", otherId, id);
        } else if (!isAdd && repository.isFriend(id, otherId)) {
            result = repository.removeRequest(id, otherId);
            log.debug("Заявка на дружбу между ID: {} и ID: {} удалена", id, otherId);
        } else {
            Activity activity = new Activity(id, EventType.FRIEND, isAdd ? Operation.ADD : Operation.REMOVE, otherId);
            activityRepository.save(activity);
            result = isAdd ? repository.addFriend(id, otherId) : repository.deleteFriend(id, otherId);
            log.debug("Друг ID: {} {} пользователю ID: {}, событие записано", otherId, isAdd ? "добавлен" : "удален", id);
        }
        return result;
    }

    private List<FilmDto> fetchAndEnrichRecommendations(Long userId, List<Long> bestRepetitionUserIds) {
        List<FilmDto> recommendations = bestRepetitionUserIds.stream()
                .flatMap(userIdBestRep -> filmRepository.getRecommendations(userId, userIdBestRep).stream())
                .map(FilmMapper::mapToFilmDto)
                .toList();
        log.debug("Получено {} фильмов для рекомендаций пользователю ID: {}", recommendations.size(), userId);

        if (!recommendations.isEmpty()) {
            recommendations.stream()
                    .filter(filmDto -> !genreRepository.getForFilm(filmDto.getId()).isEmpty())
                    .forEach(filmDto -> filmDto.setGenres(
                            genreRepository.getForFilm(filmDto.getId()).stream()
                                    .map(GenreMapper::mapToGenreDto)
                                    .toList()
                    ));
            log.debug("Жанры добавлены к рекомендациям для пользователя ID: {}", userId);
        }
        return recommendations;
    }

    private String formatNotFoundMessage(Long id) {
        return "Пользователь с ID = " + id + " не найден";
    }

    private Object[] getLogArgs(Object result, Object... args) {
        if (result instanceof Collection) return new Object[]{((Collection<?>) result).size(), args};
        if (result instanceof Boolean) return new Object[]{args[0], result};
        return new Object[]{result};
    }

    @FunctionalInterface
    private interface SupplierWithException<T> {
        T get() throws Exception;
    }
}