package ru.yandex.practicum.filmorate.service;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;


import java.util.List;

@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUser(long id) {
        return userStorage.getUser(id);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public List<User> getAllFriends(@Positive long userId) {
        User user = userStorage.getUser(userId);
        List<User> friends = user.getFriends().stream()
                .map(userStorage::getUser)
                .toList();
        log.trace("GET Запрос на список друзей пользователя с ID = {}\n{}", userId, friends);
        return friends;
    }

    public void addFriend(long userId, long friendId) {
        userStorage.getUser(userId).getFriends().add(friendId);
        userStorage.getUser(friendId).getFriends().add(userId);
        log.debug("PUT Запрос. Пользователи с ID = {} и {} стали друзьями", userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        userStorage.getUser(userId).getFriends().remove(friendId);
        userStorage.getUser(friendId).getFriends().remove(userId);
        log.debug("DELETE Запрос. Пользователь с ID = {} удалил друга с ID = {}", userId, friendId);
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        List<User> commonFriends = userStorage.getUser(userId).getFriends().stream()
                .filter(friendId -> userStorage.getUser(otherId).getFriends().contains(friendId))
                .map(userStorage::getUser)
                .toList();
        log.trace("GET Запрос на список общих друзей пользователей с ID = {} и {}:\n{}", userId, otherId, commonFriends);
        return commonFriends;
    }

}
