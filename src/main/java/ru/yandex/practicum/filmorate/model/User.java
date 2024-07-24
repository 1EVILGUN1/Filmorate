package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class User {
    private Set<Long> friends;
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
