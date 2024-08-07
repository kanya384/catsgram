package ru.yandex.practicum.catsgram.controller;

import exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.service.UserService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userService.findUserById(id);
        if (userOpt.isEmpty()) {
            throw new NotFoundException(String.format("нет пользователя с заданным id = %d", id));
        }

        return userOpt.get();
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User newUser) {
        User user = userService.create(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        return userService.update(newUser);
    }

}
