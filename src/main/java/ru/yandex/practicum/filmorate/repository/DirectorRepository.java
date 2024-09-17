package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DirectorRepository extends Repository {

    List<Director> findAll();

    Optional<Director> findById(Long id);

    boolean delete(Long id);

    Set<Director> getForFilm(Long id);

    Director save(Director director);

    Director update(Director newDirector);
}
