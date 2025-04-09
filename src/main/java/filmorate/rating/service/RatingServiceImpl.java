package filmorate.rating.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import filmorate.rating.dto.RatingDto;
import filmorate.exception.NotFoundException;
import filmorate.rating.mapper.RatingMapper;
import filmorate.rating.repository.RatingRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingServiceImpl implements RatingService {
    private final RatingRepository repository;

    public RatingDto get(Long id) {
        log.info("Запрос на получение рейтинга с ID: {}", id);
        try {
            RatingDto rating = repository.get(id)
                    .map(RatingMapper::mapToMpaDto)
                    .orElseThrow(() -> new NotFoundException("Рейтинга с ID = " + id + " не найдено"));
            log.debug("Успешно получен рейтинг: {}", rating);
            return rating;
        } catch (NotFoundException e) {
            log.warn("Рейтинг с ID: {} не найден", id);
            throw e;
        } catch (Exception e) {
            log.error("Ошибка при получении рейтинга с ID: {}", id, e);
            throw e;
        }
    }

    public List<RatingDto> getAll() {
        log.info("Запрос на получение всех рейтингов");
        try {
            List<RatingDto> ratings = repository.getAll().stream()
                    .map(RatingMapper::mapToMpaDto)
                    .collect(Collectors.toList());
            log.debug("Успешно получено {} рейтингов", ratings.size());
            return ratings;
        } catch (Exception e) {
            log.error("Ошибка при получении списка рейтингов", e);
            throw e;
        }
    }
}