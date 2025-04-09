package filmorate.genre.service;

import filmorate.genre.dto.GenreDto;


import java.util.List;

public interface GenreService {
    GenreDto get(Long id);

    List<GenreDto> getAll();
}
