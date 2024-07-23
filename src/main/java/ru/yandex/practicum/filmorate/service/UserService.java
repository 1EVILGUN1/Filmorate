package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void createFriends(int idUser, int idFriend) {
        userStorage.getUser(idFriend).setFriend(userStorage.getUser(idUser));
        userStorage.getUser(idUser).setFriend(userStorage.getUser(idUser));
    }

    public void removeFriends(int idUser, int idFriend) {
        userStorage.getUser(idFriend).deleteFriend(userStorage.getUser(idUser));
        userStorage.getUser(idUser).deleteFriend(userStorage.getUser(idFriend));
    }

    public List<User> getFriends(int idUser) {
        return userStorage.getUser(idUser).getFriends();
    }

    public List<User> getCommonFriends(int idUser, int otherId) {
        List<User> friendsUser = userStorage.getUser(idUser).getFriends();
        List<User> friendFriends = userStorage.getUser(otherId).getFriends();
        List<User> commonFriends = new ArrayList<>();
        for (User friend : friendsUser) {
            if (friendFriends.contains(friend)) {
                commonFriends.add(friend);
            }
        }
        return commonFriends;
    }
}
