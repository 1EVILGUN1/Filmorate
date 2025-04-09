package filmorate.film.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import filmorate.director.mapper.DirectorMapper;
import filmorate.director.dto.DirectorDto;
import filmorate.film.dto.FilmDto;
import filmorate.film.dto.FilmRequest;
import filmorate.genre.dto.GenreDto;
import filmorate.genre.mapper.GenreMapper;
import filmorate.director.model.Director;
import filmorate.film.model.Film;
import filmorate.genre.model.Genre;
import filmorate.rating.mapper.RatingMapper;

import java.util.List;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {

    public static FilmDto mapToFilmDto(Film film) {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDescription(film.getDescription());
        filmDto.setReleaseDate(film.getReleaseDate());
        filmDto.setDuration(film.getDuration());
        filmDto.setMpa(RatingMapper.mapToMpaDto(film.getMpa()));
        List<GenreDto> genres = film.getGenres().stream()
                .sorted(Genre::compareTo)
                .map(GenreMapper::mapToGenreDto)
                .toList();
        filmDto.setGenres(genres);
        List<DirectorDto> directors = film.getDirectors().stream()
                .sorted(Director::compareTo)
                .map(DirectorMapper::mapToDirectorDto)
                .toList();
        filmDto.setDirectors(directors);
        return filmDto;
    }

    public static Film mapToFilm(FilmRequest request) {
        Film film = new Film();
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setReleaseDate(request.getReleaseDate());
        film.setDuration(request.getDuration());
        return film;
    }

    public static Film updateFilmFields(Film film, FilmRequest request) {
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setReleaseDate(request.getReleaseDate());
        film.setDuration(request.getDuration());
        return film;
    }
}
