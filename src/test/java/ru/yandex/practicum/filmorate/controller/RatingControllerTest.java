package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.service.impl.RatingServiceImpl;

@WebMvcTest(RatingController.class)
public class RatingControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    RatingServiceImpl service;

    @Test
    public void forGetRatingByIdWhenIsExistsThenStatusOk() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/mpa/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void forGetWhenFindAllRating() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/mpa")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
