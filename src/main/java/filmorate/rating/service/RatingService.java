package filmorate.rating.service;

import filmorate.rating.dto.RatingDto;
import java.util.List;

public interface RatingService {
    RatingDto get(Long id);

    List<RatingDto> getAll();
}
