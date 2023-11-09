package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exp.EmailExistsException;
import ru.practicum.shareit.exp.NonExistentUserException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryUserStorage implements UserStorage {
    private int countId = 0;
    private final Map<Integer, User> users = new HashMap<>();
    private final Map<Integer, String> emails = new HashMap<>();

    @Override
    public User replaceUser(UserDto userDto, int userId) {
        User oldUser = getUserById(userId);
        if (userDto.getEmail() != null) {
            if (!oldUser.getEmail().equals(userDto.getEmail())) {
                checkExistEmail(userDto.getEmail());
                deleteUser(userId);
                oldUser.setEmail(userDto.getEmail());
                users.put(userId, oldUser);
                emails.put(userId, oldUser.getEmail());
            }
        }
        if (userDto.getName() != null) {
            oldUser.setName(userDto.getName());
            users.replace(userId, oldUser);
        }
        return oldUser;
    }

    @Override
    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    @Override
    public User createUser(User user) {
        checkExistEmail(user.getEmail());
        user.setId(++countId);
        users.put(user.getId(), user);
        emails.put(user.getId(), user.getEmail());
        return user;
    }

    @Override
    public User getUserById(int userId) {
        return Optional.ofNullable(users.get(userId))
                .orElseThrow(() -> new NonExistentUserException("Пользователь с таким id не найден."));
    }

    @Override
    public void deleteUser(int userId) {
        users.remove(userId);
        emails.remove(userId);
    }

    private void checkExistEmail(String email) {
        if (emails.containsValue(email)) {
            throw new EmailExistsException("Пользователь с таким email уже существует");
        }
    }
}
