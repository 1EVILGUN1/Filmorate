package filmorate.director.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import filmorate.director.dto.DirectorDto;
import filmorate.director.dto.DirectorRequest;
import filmorate.director.model.Director;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DirectorMapper {
    public static DirectorDto mapToDirectorDto(Director director) {
        DirectorDto directorDto = new DirectorDto();
        directorDto.setId(director.getId());
        directorDto.setName(director.getName());
        return directorDto;
    }

    public static Director mapToDirector(DirectorRequest request) {
        Director director = new Director();
        director.setName(request.getName());
        return director;
    }

    public static Director updateDirectorFields(Director director, DirectorRequest request) {
        director.setName(request.getName());
        return director;
    }
}
