package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.annotation.NotWriteSpace;
import ru.yandex.practicum.filmorate.validator.annotation.NullOrNotBlank;
import ru.yandex.practicum.filmorate.validator.group.Create;
import ru.yandex.practicum.filmorate.validator.group.Default;
import ru.yandex.practicum.filmorate.validator.group.Update;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @NotNull(groups = Update.class)
    @Positive(groups = Update.class)
    private Long id;

    @Email(groups = Default.class)
    @NotBlank(groups = Create.class)
    @NullOrNotBlank(groups = Update.class)
    private String email;

    @NotBlank(groups = Create.class)
    @NotWriteSpace(groups = Default.class)
    @NullOrNotBlank(groups = Update.class)
    private String login;

    private String name;

    @PastOrPresent(groups = Default.class)
    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();
}