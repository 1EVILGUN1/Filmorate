package ru.yandex.practicum.filmorate.controller.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
