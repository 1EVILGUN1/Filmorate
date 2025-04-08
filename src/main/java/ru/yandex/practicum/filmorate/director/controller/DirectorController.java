package ru.yandex.practicum.filmorate.director.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.director.dto.DirectorDto;
import ru.yandex.practicum.filmorate.director.dto.DirectorRequest;
import ru.yandex.practicum.filmorate.director.service.DirectorService;

import java.util.Collection;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
@Slf4j
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<DirectorDto> getDirectors() {
        log.info("Получен запрос на получение всех режиссеров");
        Collection<DirectorDto> directors = directorService.getAll();
        log.debug("Возвращаем список всех режиссеров, количество: {}", directors.size());
        return directors;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DirectorDto getDirector(@PathVariable("id") Long id) {
        log.info("Получен запрос на получение режиссера с ID: {}", id);
        DirectorDto director = directorService.get(id);
        log.debug("Возвращаем режиссера: {}", director);
        return director;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean deleteDirector(@PathVariable("id") Long id) {
        log.info("Получен запрос на удаление режиссера с ID: {}", id);
        boolean isDeleted = directorService.delete(id);
        log.debug("Результат удаления режиссера с ID {}: {}", id, isDeleted);
        return isDeleted;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public DirectorDto createDirector(@Valid @RequestBody DirectorRequest director) {
        log.info("Получен запрос на создание нового режиссера: {}", director);
        DirectorDto createdDirector = directorService.save(director);
        log.debug("Создан новый режиссер: {}", createdDirector);
        return createdDirector;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public DirectorDto updateDirector(@Valid @RequestBody DirectorRequest newDirector) {
        log.info("Получен запрос на обновление режиссера: {}", newDirector);
        DirectorDto updatedDirector = directorService.update(newDirector);
        log.debug("Обновлен режиссер: {}", updatedDirector);
        return updatedDirector;
    }
}