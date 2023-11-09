package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        return userStorage.getAllUsers()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
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
