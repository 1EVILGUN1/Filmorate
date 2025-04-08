package ru.yandex.practicum.filmorate.director.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.director.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.director.dto.DirectorDto;
import ru.yandex.practicum.filmorate.director.dto.DirectorRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.director.model.Director;
import ru.yandex.practicum.filmorate.director.repository.DirectorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectorServiceImpl implements DirectorService {
    private final DirectorRepository repository;

    public List<DirectorDto> getAll() {
        log.info("Запрос всех режиссеров из базы данных");
        List<DirectorDto> directors = repository.findAll()
                .stream()
                .map(DirectorMapper::mapToDirectorDto)
                .collect(Collectors.toList());
        log.debug("Найдено {} режиссеров", directors.size());
        return directors;
    }

    public DirectorDto get(Long id) {
        log.info("Запрос режиссера с ID: {}", id);
        DirectorDto director = repository.findById(id)
                .map(DirectorMapper::mapToDirectorDto)
                .orElseThrow(() -> {
                    log.error("Режиссёр с ID = {} не найден", id);
                    return new NotFoundException("Режиссёр с ID = " + id + " не найден");
                });
        log.debug("Найден режиссер: {}", director);
        return director;
    }

    public boolean delete(Long id) {
        log.info("Удаление режиссера с ID: {}", id);
        boolean isDeleted = repository.delete(id);
        if (isDeleted) {
            log.debug("Режиссер с ID {} успешно удален", id);
        } else {
            log.warn("Не удалось удалить режиссера с ID {}", id);
        }
        return isDeleted;
    }

    public DirectorDto save(DirectorRequest request) {
        log.info("Создание нового режиссера: {}", request);
        Director director = DirectorMapper.mapToDirector(request);
        director = repository.save(director);
        DirectorDto savedDirector = DirectorMapper.mapToDirectorDto(director);
        log.debug("Создан новый режиссер: {}", savedDirector);
        return savedDirector;
    }

    public DirectorDto update(DirectorRequest request) {
        log.info("Обновление режиссера с ID: {}", request.getId());
        Director updatedDirector = repository.findById(request.getId())
                .map(director -> {
                    log.debug("Найден режиссер для обновления: {}", director);
                    return DirectorMapper.updateDirectorFields(director, request);
                })
                .orElseThrow(() -> {
                    log.error("Режиссёр с id = {} не существует", request.getId());
                    return new NotFoundException("Режиссёра с id = " + request.getId() + " не существует");
                });

        updatedDirector = repository.update(updatedDirector);
        DirectorDto result = DirectorMapper.mapToDirectorDto(updatedDirector);
        log.debug("Обновленный режиссер: {}", result);
        return result;
    }
}