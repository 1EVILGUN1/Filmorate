package ru.yandex.practicum.filmorate.genre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.genre.dto.GenreDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.genre.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.genre.repository.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {
    private final GenreRepository repository;

    public GenreDto get(Long id) {
        log.info("Запрос на получение жанра с ID: {}", id);
        try {
            GenreDto genre = repository.findById(id)
                    .map(GenreMapper::mapToGenreDto)
                    .orElseThrow(() -> new NotFoundException("Жанр с ID = " + id + " не найден"));
            log.debug("Успешно получен жанр: {}", genre);
            return genre;
        } catch (NotFoundException e) {
            log.warn("Жанр с ID: {} не найден", id);
            throw e;
        } catch (Exception e) {
            log.error("Ошибка при получении жанра с ID: {}", id, e);
            throw e;
        }
    }

    public List<GenreDto> getAll() {
        log.info("Запрос на получение всех жанров");
        try {
            List<GenreDto> genres = repository.getAll().stream()
                    .map(GenreMapper::mapToGenreDto)
                    .collect(Collectors.toList());
            log.debug("Успешно получено {} жанров", genres.size());
            return genres;
        } catch (Exception e) {
            log.error("Ошибка при получении списка жанров", e);
            throw e;
        }
    }
}