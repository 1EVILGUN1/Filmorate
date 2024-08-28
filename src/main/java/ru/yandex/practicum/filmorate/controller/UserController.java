package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.group.Create;
import ru.yandex.practicum.filmorate.validator.group.Default;
import ru.yandex.practicum.filmorate.validator.group.Update;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Validated({Create.class, Default.class}) User user) {
        log.info("GET Запрос на создание пользователя {}", user);
        User createdUser = userService.createUser(user);
        log.info("GET Запрос на создание пользователя выполнен {}", createdUser);
        return createdUser;
    }

    @PutMapping
    public User updateUser(@RequestBody @Validated({Update.class, Default.class}) User user) {
        log.info("PUT Запрос на обновление данных пользователя {}", user);
        User updatedUser = userService.updateUser(user);
        log.info("PUT Запрос на обновление данных пользователя выполнен {}", updatedUser);
        return updatedUser;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") @Positive long id) {
        log.info("GET Запрос на поиск пользователя по id {}", id);
        User user = userService.getUser(id);
        log.info("GET Запрос на поиск пользователя выполнен {}", user);
        return user;
    }

    @GetMapping()
    public Collection<User> getUsers() {
        log.info("GET Запрос на список пользователей");
        Collection<User> users = userService.getUsers();
        log.info("GET Запрос на список пользователей выполнен {}", users);
        return users;
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") @Positive int id) {
        log.info("GET Запрос на список друзей пользователя id {}", id);
        List<User> userFriends = userService.getAllFriends(id);
        log.info("GET Запрос на список друзей пользователя выполнен {}", userFriends);
        return userFriends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable("id") @Positive long id, @PathVariable("otherId") @Positive long otherId) {
        log.info("GET Запрос на список общих друзей пользователя id {} и пользователя id {}", id, otherId);
        List<User> userMutualFriends = userService.getMutualFriends(id, otherId);
        log.info("GET Запрос на список общих друзей выполнен {}", userMutualFriends);
        return userMutualFriends;
    }


    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFriend(@PathVariable @Positive long id, @PathVariable @Positive long friendId) {
        log.info("PUT Запрос на добавление пользователем id {} в друзья пользователя id {}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable("id") @Positive @NotNull long id, @PathVariable("friendId") @Positive @NotNull long friendId) {
        log.info("DELETE Запрос на удаление пользователем id {} друга пользователя id {}", id, friendId);
        userService.removeFromFriends(id, friendId);
    }

    @DeleteMapping(value = "/{userId}")
    public void deleteUser(@NotNull @PathVariable long userId) {
        log.info("DELETE Запрос на удаление пользователя id {}", userId);
        userService.deleteUser(userId);
    }
}
