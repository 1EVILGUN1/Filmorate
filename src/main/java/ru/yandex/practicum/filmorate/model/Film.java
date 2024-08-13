package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.annotation.NullOrNotBlank;
import ru.yandex.practicum.filmorate.validator.annotation.ReleaseDate;
import ru.yandex.practicum.filmorate.validator.group.Create;
import ru.yandex.practicum.filmorate.validator.group.Default;
import ru.yandex.practicum.filmorate.validator.group.Update;

import java.time.LocalDate;
import java.util.*;


@Data
@Builder
public class Film {
    @NotNull(groups = Update.class)
    @Positive(groups = Update.class)
    private Long id;

    @NotBlank(groups = Create.class)
    @NullOrNotBlank(groups = Update.class)
    private String name;

    @Size(max = 200, groups = Default.class)
    private String description;

    @ReleaseDate(groups = Default.class)
    private LocalDate releaseDate;

    @Positive(groups = Default.class)
    private Integer duration;

    @NotNull
    private final Mpa mpa;
    private final Set<Long> likesFilm = new HashSet<>();
    private final Set<Genre> genres = new LinkedHashSet<>();

}
