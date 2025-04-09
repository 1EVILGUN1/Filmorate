package filmorate.rating.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import filmorate.rating.dto.RatingDto;
import filmorate.rating.model.Rating;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RatingMapper {
    public static RatingDto mapToMpaDto(Rating mpa) {
        RatingDto mpaDto = new RatingDto();
        mpaDto.setId(mpa.getId());
        mpaDto.setName(mpa.getName());
        return mpaDto;
    }

    public static Rating mapToMpa(RatingDto mpaDto) {
        Rating mpa = new Rating();
        mpa.setId(mpaDto.getId());
        mpa.setName(mpaDto.getName());
        return mpa;
    }
}
