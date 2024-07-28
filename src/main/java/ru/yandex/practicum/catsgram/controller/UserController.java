package ru.yandex.practicum.catsgram.controller;

import exception.ConditionsNotMetException;
import exception.DuplicateDataException;
import exception.NotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return  users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }

        if (isEmailExists(user.getEmail())) {
            throw new DuplicateDataException("Этот имейл уже используется");
        }

        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());

        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        if (!users.containsKey(newUser.getId())) {
            throw new NotFoundException(String.format("User с id = %d не найден", newUser.getId()));
        }

        User oldUser = users.get(newUser.getId());

        if (oldUser != null && newUser.getEmail() != null && !oldUser.getEmail().equals(newUser.getEmail()) && isEmailExists(newUser.getEmail())) {
            throw new DuplicateDataException("Этот имейл уже используется");
        }

        if (newUser.getEmail() == null) {
            newUser.setEmail(oldUser.getEmail());
        }

        if (newUser.getUsername() == null) {
            newUser.setUsername(oldUser.getUsername());
        }

        if (newUser.getPassword() == null) {
            newUser.setPassword(oldUser.getPassword());
        }

        users.put(newUser.getId(), newUser);

        return newUser;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private boolean isEmailExists(String search) {
        return users.values()
                .stream()
                .map(User::getEmail)
                .anyMatch(email -> email.equals(search));
    }
}
