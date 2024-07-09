package ru.yandex.practicum.filmorate;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.manager.Manager;

@SpringBootApplication
public class FilmorateApplication {
    private static final Logger log = LoggerFactory.getLogger(Manager.class);
    public static void main(String[] args) {
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME))
                .setLevel(Level.INFO);
        SpringApplication.run(FilmorateApplication.class, args);
        log.info("Application started");
    }

}
