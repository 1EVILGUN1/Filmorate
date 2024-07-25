package ru.yandex.practicum.filmorate.controller;

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
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Validated({Update.class, Default.class}) User user) {
        log.info("PUT Запрос на обновление данных пользователя {}", user);
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") @Positive long id) {
        log.info("GET Запрос на поиск пользователя по id {}", id);
        return userService.getUser(id);
    }

    @GetMapping()
    public List<User> getUsers() {
        log.info("GET Запрос на список пользователей");
        return userService.getUsers();
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") @Positive int id) {
        log.info("GET Запрос на список друзей пользователя id {}", id);
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") @Positive long id, @PathVariable("otherId") @Positive long otherId) {
        log.info("GET Запрос на список общих друзей пользователя id {} и пользователя id {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }


    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFriend(@PathVariable @Positive long id, @PathVariable @Positive long friendId) {
        log.info("PUT Запрос на добавление пользователем id {} в друзья пользователя id {}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriend(@PathVariable("id") @Positive long id, @PathVariable("friendId") @Positive long friendId) {
        log.info("DELETE Запрос на удаление пользователем id {} друга пользователя id {}", id, friendId);
        userService.removeFriend(id, friendId);
    }
}
