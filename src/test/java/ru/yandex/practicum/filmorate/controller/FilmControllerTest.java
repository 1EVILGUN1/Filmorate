package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.impl.FilmServiceImpl;

import java.time.LocalDate;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {
    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    @MockBean
    FilmServiceImpl service;

    @Autowired
    public FilmControllerTest(MockMvc mvc, ObjectMapper objectMapper) {
        this.mvc = mvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void forPostWhenFilmWithCorrectDataThenStatusSuccessful() throws Exception {
        Film film = new Film();
        film.setName("test");
        film.setDescription("test");
        film.setReleaseDate(LocalDate.parse("2000-01-01"));
        film.setDuration(90);
        mvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void forPostWhenFilmWithoutNameThenStatus400() throws Exception {
        Film film = new Film();
        film.setDescription("test");
        mvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void forPostWhenFilmWithBlankNameThenStatus400() throws Exception {
        Film film = new Film();
        film.setName("");
        film.setDescription("test");
        mvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void forPostWhenFilmWithExceededLengthThenStatus400() throws Exception {
        Film film = new Film();
        film.setName("test");
        film.setDescription("test".repeat(50) + "!");
        mvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void forPostWhenFilmWithReleaseDateBeforeMim() throws Exception {
        Film film = new Film();
        film.setName("test");
        film.setDescription("test");
        film.setReleaseDate(LocalDate.parse("1895-12-27"));
        film.setDuration(90);
        mvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void forPostWhenFilmWithNegativeDuration() throws Exception {
        Film film = new Film();
        film.setName("test");
        film.setDescription("test");
        film.setDuration(-1);
        mvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
