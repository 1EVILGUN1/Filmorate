package filmorate.genre.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import filmorate.genre.dto.GenreDto;
import filmorate.genre.model.Genre;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenreMapper {
    public static GenreDto mapToGenreDto(Genre genre) {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(genre.getId());
        genreDto.setName(genre.getName());
        return genreDto;
    }
}
