DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS friendsship CASCADE;
DROP TABLE IF EXISTS rating CASCADE;
DROP TABLE IF EXISTS film CASCADE;
DROP TABLE IF EXISTS film_like CASCADE;
DROP TABLE IF EXISTS genre CASCADE;
DROP TABLE IF EXISTS film_genre CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id  INTEGER               NOT NULL AUTO_INCREMENT,
    email    CHARACTER VARYING(50) NOT NULL,
    login    CHARACTER VARYING(30) NOT NULL,
    name     CHARACTER VARYING(30) NOT NULL,
    birthday DATE                  NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS friendship
(
    user_first_id  INTEGER NOT NULL,
    user_second_id INTEGER NOT NULL,
    CONSTRAINT friendship_fk FOREIGN KEY (user_first_id) REFERENCES users (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT friendship_fk_1 FOREIGN KEY (user_second_id) REFERENCES users (id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS genre
(
    id   INTEGER               NOT NULL AUTO_INCREMENT,
    name CHARACTER VARYING(20) NOT NULL,
    CONSTRAINT genre_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS rating
(
    id   INTEGER               NOT NULL AUTO_INCREMENT,
    name CHARACTER VARYING(10) NOT NULL,
    CONSTRAINT rating_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS film
(
    id      INTEGER                NOT NULL AUTO_INCREMENT,
    name         CHARACTER VARYING(30)  NOT NULL,
    description  CHARACTER VARYING(200) NOT NULL,
    release_date DATE                   NOT NULL,
    duration     INTEGER                NOT NULL,
    rating_id    INTEGER                NOT NULL,
    CONSTRAINT film_pk PRIMARY KEY (id),
    CONSTRAINT film_fk FOREIGN KEY (rating_id) REFERENCES rating (id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS film_like
(
    film_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    CONSTRAINT film_like_pk PRIMARY KEY (user_id, film_id),
    CONSTRAINT film_like_fk FOREIGN KEY (film_id) REFERENCES film (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT film_like_fk_1 FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id  INTEGER NOT NULL,
    genre_id INTEGER NOT NULL,
    CONSTRAINT film_genre_pk PRIMARY KEY (film_id, genre_id),
    CONSTRAINT film_genre_fk FOREIGN KEY (film_id) REFERENCES film (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT film_genre_fk_1 FOREIGN KEY (genre_id) REFERENCES genre (id)
        ON DELETE CASCADE ON UPDATE CASCADE
);