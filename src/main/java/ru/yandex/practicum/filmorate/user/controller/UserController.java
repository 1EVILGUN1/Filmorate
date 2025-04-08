package ru.yandex.practicum.filmorate.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.activity.dto.ActivityDto;
import ru.yandex.practicum.filmorate.film.dto.FilmDto;
import ru.yandex.practicum.filmorate.user.dto.UserRequest;
import ru.yandex.practicum.filmorate.user.dto.UserDto;
import ru.yandex.practicum.filmorate.user.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;

    @GetMapping
    public Collection<UserDto> getUsers() {
        log.info("Получен GET запрос на получение всех пользователей");
        try {
            Collection<UserDto> users = service.getAll();
            log.debug("Успешно возвращено {} пользователей", users.size());
            return users;
        } catch (Exception e) {
            log.error("Ошибка при получении списка пользователей", e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        log.info("Получен GET запрос на получение пользователя с ID: {}", id);
        try {
            UserDto user = service.get(id);
            log.debug("Успешно получен пользователь: {}", user);
            return user;
        } catch (Exception e) {
            log.error("Ошибка при получении пользователя с ID: {}", id, e);
            throw e;
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserRequest request) {
        log.info("Получен POST запрос на создание пользователя: {}", request);
        try {
            UserDto user = service.save(request);
            log.debug("Успешно создан пользователь с ID: {}", user.getId());
            return user;
        } catch (Exception e) {
            log.error("Ошибка при создании пользователя: {}", request, e);
            throw e;
        }
    }

    @PutMapping
    public UserDto updateUser(@Valid @RequestBody UserRequest request) {
        log.info("Получен PUT запрос на обновление пользователя: {}", request);
        try {
            UserDto user = service.update(request);
            log.debug("Успешно обновлен пользователь с ID: {}", user.getId());
            return user;
        } catch (Exception e) {
            log.error("Ошибка при обновлении пользователя: {}", request, e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable Long id) {
        log.info("Получен DELETE запрос на удаление пользователя с ID: {}", id);
        try {
            boolean isDeleted = service.delete(id);
            log.debug("Результат удаления пользователя с ID: {} - {}", id, isDeleted);
            return isDeleted;
        } catch (Exception e) {
            log.error("Ошибка при удалении пользователя с ID: {}", id, e);
            throw e;
        }
    }

    @GetMapping("/{id}/friends")
    public List<UserDto> getFriends(@PathVariable Long id) {
        log.info("Получен GET запрос на получение друзей пользователя с ID: {}", id);
        try {
            List<UserDto> friends = service.getFriends(id);
            log.debug("Успешно возвращено {} друзей для пользователя ID: {}", friends.size(), id);
            return friends;
        } catch (Exception e) {
            log.error("Ошибка при получении друзей пользователя с ID: {}", id, e);
            throw e;
        }
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<UserDto> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Получен GET запрос на получение общих друзей пользователей ID: {} и ID: {}", id, otherId);
        try {
            Set<UserDto> commonFriends = service.getCommonFriends(id, otherId);
            log.debug("Успешно возвращено {} общих друзей для пользователей ID: {} и ID: {}", commonFriends.size(), id, otherId);
            return commonFriends;
        } catch (Exception e) {
            log.error("Ошибка при получении общих друзей пользователей ID: {} и ID: {}", id, otherId, e);
            throw e;
        }
    }

    @PutMapping("/{id}/friends/{friendsId}")
    public boolean addFriend(@PathVariable Long id, @PathVariable Long friendsId) {
        log.info("Получен PUT запрос на добавление друга ID: {} пользователю ID: {}", friendsId, id);
        try {
            boolean isAdded = service.addFriend(id, friendsId);
            log.debug("Результат добавления друга ID: {} для пользователя ID: {} - {}", friendsId, id, isAdded);
            return isAdded;
        } catch (Exception e) {
            log.error("Ошибка при добавлении друга ID: {} пользователю ID: {}", friendsId, id, e);
            throw e;
        }
    }

    @DeleteMapping("/{id}/friends/{friendsId}")
    public boolean deleteFriend(@PathVariable Long id, @PathVariable Long friendsId) {
        log.info("Получен DELETE запрос на удаление друга ID: {} у пользователя ID: {}", friendsId, id);
        try {
            boolean isDeleted = service.deleteFriend(id, friendsId);
            log.debug("Результат удаления друга ID: {} у пользователя ID: {} - {}", friendsId, id, isDeleted);
            return isDeleted;
        } catch (Exception e) {
            log.error("Ошибка при удалении друга ID: {} у пользователя ID: {}", friendsId, id, e);
            throw e;
        }
    }

    @GetMapping("/{id}/recommendations")
    public List<FilmDto> getRecommendations(@PathVariable(value = "id") Long userId) {
        log.info("Получен GET запрос на получение рекомендаций для пользователя ID: {}", userId);
        try {
            List<FilmDto> recommendations = service.getRecommendations(userId);
            log.debug("Успешно возвращено {} рекомендаций для пользователя ID: {}", recommendations.size(), userId);
            return recommendations;
        } catch (Exception e) {
            log.error("Ошибка при получении рекомендаций для пользователя ID: {}", userId, e);
            throw e;
        }
    }

    @GetMapping("{id}/feed")
    public List<ActivityDto> getUserFeed(@PathVariable Long id) {
        log.info("Получен GET запрос на получение ленты активности пользователя ID: {}", id);
        try {
            List<ActivityDto> feed = service.getUserFeed(id);
            log.debug("Успешно возвращено {} событий в ленте пользователя ID: {}", feed.size(), id);
            return feed;
        } catch (Exception e) {
            log.error("Ошибка при получении ленты активности пользователя ID: {}", id, e);
            throw e;
        }
    }
}