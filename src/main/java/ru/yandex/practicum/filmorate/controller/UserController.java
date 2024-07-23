package ru.yandex.practicum.filmorate.controller;


import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final InMemoryUserStorage userStorage = new InMemoryUserStorage();

    public UserController() {
        this.userService = new UserService(userStorage);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") int id) {
        return userStorage.getUser(id);
    }

    @GetMapping()
    public List<User> getListUsers() {
        return userStorage.getUsers();
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") int id) {
        if (userStorage.getUser(id) == null) {
            throw new ValidationException("GET запрос не выполнен. Данного пользователя нет");
        }
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getFriendsCommon(@PathVariable("id") int id, @PathVariable("otherId") int otherId) {
        if (userStorage.getUser(id) == null || userStorage.getUser(otherId) == null) {
            throw new ValidationException("GET запрос не выполнен. Данного пользователя нет");
        }
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User createUserWeb(@RequestBody User user) {
        return userStorage.createUser(user);
    }

    @PutMapping
    public User updateUserWeb(@RequestBody User user) {
        if (userStorage.updateUser(user) == null) {
            throw new ValidationException("PUT запрос не выполнен на изменение пользователя");
        }
        return userStorage.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void createFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        if (userStorage.getUser(id) == null || userStorage.getUser(friendId) == null) {
            throw new ValidationException("PUT запрос не выполнен. Данного пользователя нет");
        }
        userService.createFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        if (userStorage.getUser(id) == null || userStorage.getUser(friendId) == null) {
            throw new ValidationException("DELETE запрос не выполнен. Данного пользователя нет");
        }
        userService.removeFriends(id, friendId);
    }
}
