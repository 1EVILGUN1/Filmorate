package ru.yandex.practicum.filmorate.controller.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UserRequest {
    private Long id;
    @NotBlank(message = "Пустой E-mail")
    @Email(message = "Некорректный E-mail")
    private String email;
    @NotBlank(message = "Пустой логин")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$")
    private String login;
    private String name;
    @Past(message = "Некорректная дата рождения")
    private LocalDate birthday;
    private Set<Long> friendsSet;
}
