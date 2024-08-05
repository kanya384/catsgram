package ru.yandex.practicum.catsgram.service;

import exception.ConditionsNotMetException;
import exception.DuplicateDataException;
import exception.NotFoundException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User create(User user) {
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

    public User update(User newUser) {
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

    public Optional<User> findUserById(long userId) {
        return Optional.ofNullable(users.get(userId));
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
