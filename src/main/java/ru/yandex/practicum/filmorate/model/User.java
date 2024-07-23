package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

@Data
public class User {
    private List<User> friends;
    private List<Film> likesFilm;
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public void setFriend(User friend) {
        friends.add(friend);
    }

    public void deleteFriend(User friend) {
        friends.remove(friend);
    }
}
