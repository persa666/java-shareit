package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exp.EmailExistsException;
import ru.practicum.shareit.exp.NonExistentUserException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryUserStorage implements UserStorage {
    private int countId = 0;
    private final Map<String, User> users = new HashMap<>();

    @Override
    public User replaceUser(UserDto userDto, int userId) {
        User oldUser = getUserById(userId);
        if (userDto.getEmail() != null) {
            if (!oldUser.getEmail().equals(userDto.getEmail())) {
                checkExistEmail(userDto.getEmail());
                users.remove(oldUser.getEmail());
                oldUser.setEmail(userDto.getEmail());
                users.put(oldUser.getEmail(), oldUser);
            }
        }
        if (userDto.getName() != null) {
            oldUser.setName(userDto.getName());
            users.replace(oldUser.getEmail(), oldUser);
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
        users.put(user.getEmail(), user);
        return user;
    }

    @Override
    public User getUserById(int userId) {
        for (User elem : users.values()) {
            if (elem.getId() == userId) {
                return elem;
            }
        }
        throw new NonExistentUserException("Пользователя с таким id не существует");
    }

    @Override
    public void deleteUser(int userId) {
        users.remove(getUserById(userId).getEmail());
    }

    private void checkExistEmail(String email) {
        if (users.containsKey(email)) {
            throw new EmailExistsException("Пользователь с таким email уже существует");
        }
    }
}
