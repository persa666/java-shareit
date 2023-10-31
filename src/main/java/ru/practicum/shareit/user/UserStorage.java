package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();

    User createUser(User user);

    User getUserById(int userId);

    User replaceUser(UserDto userDto, int userId);

    void deleteUser(int userId);
}
