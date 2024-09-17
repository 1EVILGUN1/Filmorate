package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public Collection<Mpa> getAllMpa() {
        log.info("GET Запрос на получение списка рейтингов");
        Collection<Mpa> mpas = mpaService.getAllMpa();
        log.info("GET Запрос на получение списка рейтингов выполнен {}", mpas);
        return mpas;
    }

    @GetMapping("/{id}")
    public Mpa getMpaById(@NotNull @PathVariable int id) {
        log.info("GET Запрос на получение рейтинга по id {}", id);
        Mpa mpa = mpaService.getMpaById(id);
        log.info("GET Запрос на получение рейтинга выполнен {}", mpa);
        return mpa;
    }
}
