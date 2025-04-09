package filmorate.rating.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import filmorate.pubRepository.BaseRepository;
import filmorate.rating.model.Rating;

import java.util.List;
import java.util.Optional;

@Repository
public class RatingRepositoryImpl extends BaseRepository<Rating> implements RatingRepository {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM ratings WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM ratings";

    public RatingRepositoryImpl(JdbcTemplate jdbc, RowMapper<Rating> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Rating> get(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<Rating> getAll() {
        return findMany(FIND_ALL_QUERY);
    }
}
