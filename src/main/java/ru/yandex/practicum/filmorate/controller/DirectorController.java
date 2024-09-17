package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.model.director.DirectorDto;
import ru.yandex.practicum.filmorate.controller.model.director.DirectorRequest;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.Collection;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<DirectorDto> getDirectors() {
        return directorService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DirectorDto getDirector(@PathVariable("id") Long id) {
        return directorService.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean deleteDirector(@PathVariable("id") Long id) {
        return directorService.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public DirectorDto createDirector(@Valid @RequestBody DirectorRequest director) {
        return directorService.save(director);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public DirectorDto updateDirector(@Valid @RequestBody DirectorRequest newDirector) {
        return directorService.update(newDirector);
    }
}
