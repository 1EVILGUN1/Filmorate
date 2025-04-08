package ru.yandex.practicum.filmorate.director.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DirectorRequest {
    private Long id;
    @NotBlank(message = "Пустое имя")
    private String name;
}
