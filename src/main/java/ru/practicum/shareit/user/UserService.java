package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto createUser(UserDto userDto);

    UserDto getUserById(int userId);

    UserDto replaceUser(UserDto userDto, int userId);

    void deleteUser(int userId);
}
