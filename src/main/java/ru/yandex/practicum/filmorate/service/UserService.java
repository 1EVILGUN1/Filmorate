package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.inheritance.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FilmService filmService;

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void deleteUser(long userId) {
        userStorage.deleteUser(userId);
    }

    public User getUser(long id) {
        return userStorage.getUser(id);
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public void addFriend(long userId, long friendId) {
        userStorage.addFriend(userId, friendId);
    }

    public void removeFromFriends(long userId, long friendId) {
        userStorage.removeFromFriends(userId, friendId);
    }

    public List<User> getMutualFriends(long userId, long otherId) {
        return userStorage.getMutualFriends(userId, otherId);
    }

    public List<User> getAllFriends(long userId) {
        return userStorage.getAllFriends(userId);
    }

    public List<Film> getRecommendations(long userId) {
        getUser(userId);
        return filmService.getRecommendations(userId);
    }
}
