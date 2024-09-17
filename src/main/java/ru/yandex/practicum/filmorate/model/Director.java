package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Director implements Comparable<Director> {
    private Long id;
    private String name;

    @Override
    public int compareTo(Director o) {
        return this.id.compareTo(o.getId());
    }
}
