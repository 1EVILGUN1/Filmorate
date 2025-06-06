package filmorate.genre.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Genre implements Comparable<Genre> {
    private Long id;
    private String name;

    @Override
    public int compareTo(Genre o) {
        return this.id.compareTo(o.getId());
    }
}
