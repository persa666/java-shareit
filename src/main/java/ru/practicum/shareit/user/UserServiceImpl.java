package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exp.EmailExistsException;
import ru.practicum.shareit.exp.NonExistentBookingException;
import ru.practicum.shareit.exp.NonExistentUserException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto replaceUser(UserDto userDto, int userId) {
        try {
            if (userDto.getEmail() != null) {
                if (userDto.getName() != null) {
                    userRepository.saveUserById(userDto.getName(), userDto.getEmail(),
                            userId);
                } else {
                    userRepository.saveUserEmailById(userDto.getEmail(), userId);
                }
            } else {
                userRepository.saveUserNameById(userDto.getName(), userId);
            }
            return UserMapper.toUserDto(userRepository.findById(userId).orElseThrow(() ->
                    new NonExistentBookingException("Пользователь с таким id не найден.")));
        } catch (RuntimeException e) {
            throw new EmailExistsException("Пользователь с таким email уже существует");
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        try {
            return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
        } catch (RuntimeException e) {
            throw new EmailExistsException("Пользователь с таким email уже существует");
        }
    }

    @Override
    public UserDto getUserById(int userId) {
        try {
            return UserMapper.toUserDto(userRepository.findById(userId).orElseThrow(() ->
                    new NonExistentBookingException("Пользователь с таким id не найден.")));
        } catch (RuntimeException e) {
            throw new NonExistentUserException("Пользователь с таким id не найден.");
        }
    }

    @Override
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }
}
