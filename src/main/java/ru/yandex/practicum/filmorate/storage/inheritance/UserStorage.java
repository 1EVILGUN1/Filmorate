package ru.yandex.practicum.filmorate.storage.inheritance;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    void deleteUser(long userId);

    User getUser(long id);

    Collection<User> getUsers();

    void addFriend(long userId, long friendId);

    void removeFromFriends(long userId, long friendId);

    List<User> getAllFriends(long userId);

    List<User> getMutualFriends(long userId, long otherUserId);
}
