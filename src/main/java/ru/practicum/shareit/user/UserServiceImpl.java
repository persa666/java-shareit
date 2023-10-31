package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto replaceUser(UserDto userDto, int userId) {
        return UserMapper.toUserDto(userStorage.replaceUser(userDto, userId));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> users = new ArrayList<>();
        for (User elem : userStorage.getAllUsers()) {
            users.add(UserMapper.toUserDto(elem));
        }
        return users;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        return UserMapper.toUserDto(userStorage.createUser(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto getUserById(int userId) {
        return UserMapper.toUserDto(userStorage.getUserById(userId));
    }

    @Override
    public void deleteUser(int userId) {
        userStorage.deleteUser(userId);
    }
}
