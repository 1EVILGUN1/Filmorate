package filmorate.review.model;

import lombok.Data;
import filmorate.film.model.Film;
import filmorate.user.model.User;

@Data
public class Review {
    private Long id;
    private String content;
    private Boolean isPositive;
    private User user;
    private Film film;
    private Integer useful;
}
