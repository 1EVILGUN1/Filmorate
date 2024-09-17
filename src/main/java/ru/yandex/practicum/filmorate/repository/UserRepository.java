package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends Repository {
    Optional<User> findById(Long id);

    List<User> getAll();

    User save(User user);

    User update(User newUser);

    boolean delete(Long id);

    Set<User> getFriends(Long id);

    Set<User> getCommonFriends(Object... params);

    boolean addFriend(Object... params);

    boolean deleteFriend(Object... params);

    boolean isFriendRequest(Object... params);

    boolean acceptRequest(Object... params);

    boolean isFriend(Object... params);

    boolean removeRequest(Object... params);

    List<Long> getBestRepetitionUserIds(Long userId);
}
