package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public User createUser(User user);

    public User updateUser(User user);

    public User getUser(int id);

    public List<User> getUsers();
}
