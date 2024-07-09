package ru.yandex.practicum.filmorate.controller;


import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.manager.Manager;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    Manager manager = new Manager();

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") int id) {
        return manager.getUser(id);
    }

    @GetMapping()
    public List<User> getListUsers() {
        if(manager.getUsers()==null) {
            throw new ValidationException("error.size 0");
        }
        return manager.getUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        if(manager.createUser(user) == null){
            throw new ValidationException("error.createUser");
        }
        return manager.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (manager.updateUser(user) == null) {
            throw new ValidationException("error.updateUser");
        }
        return manager.updateUser(user);
    }
}
