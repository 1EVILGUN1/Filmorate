package ru.yandex.practicum.filmorate.controller.model.director;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DirectorRequest {
    private Long id;
    @NotBlank(message = "Пустое имя")
    private String name;
}
