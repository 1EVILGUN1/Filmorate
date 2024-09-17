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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.impl.UserServiceImpl;

import java.time.LocalDate;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    @MockBean
    UserServiceImpl userServiceImpl;

    @Autowired
    public UserControllerTest(MockMvc mvc, ObjectMapper objectMapper) {
        this.mvc = mvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void forPostWhenUserWithCorrectDataThenStatusSuccessful() throws Exception {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("test");
        user.setName("test");
        user.setBirthday(LocalDate.parse("2000-01-01"));
        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void forPostWhenUserWithoutLoginThenStatus400() throws Exception {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setName("test");
        mvc.perform(MockMvcRequestBuilders.post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void forPostWhenUserWithBlankLoginThenStatus400() throws Exception {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("");
        user.setName("test");
        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void forPostWhenUserWithBlankEmailThenStatus400() throws Exception {
        User user = new User();
        user.setEmail("");
        user.setLogin("test");
        user.setName("test");
        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void forPostWhenUserWithUncorrectedEmailThenStatus400() throws Exception {
        User user = new User();
        user.setEmail("test");
        user.setLogin("test");
        user.setName("test");
        user.setBirthday(LocalDate.parse("2000-01-01"));
        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void forPostWhenUserWithBirthdayInFutureThenStatus400() throws Exception {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("test");
        user.setName("test");
        user.setBirthday(LocalDate.parse("3000-01-01"));
        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
