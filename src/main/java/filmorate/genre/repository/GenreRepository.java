package filmorate.genre.repository;

import filmorate.genre.model.Genre;
import filmorate.pubRepository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository extends Repository {
    Optional<Genre> findById(Long id);

    List<Genre> getAll();

    Set<Genre> getForFilm(Long id);
}
