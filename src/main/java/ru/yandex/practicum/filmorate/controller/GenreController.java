package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.model.genre.GenreDto;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GenreDto get(@PathVariable Long id) {
        return genreService.get(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GenreDto> getAll() {
        return genreService.getAll();
    }
}
