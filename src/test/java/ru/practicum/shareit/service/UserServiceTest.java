package ru.practicum.shareit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exp.EmailExistsException;
import ru.practicum.shareit.exp.NonExistentUserException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Transactional
    @Test
    void deleteUserTest() {
        int userId = 1;

        userService.deleteUser(userId);

        Mockito.verify(userRepository, Mockito.times(1)).deleteById(userId);
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Transactional
    @Test
    void createUserSuccessTest() {
        UserDto userDto = new UserDto(1, "John Doe", "john.doe@example.com");
        User user = new User(1, "John Doe", "john.doe@example.com");

        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        UserDto createdUser = userService.createUser(userDto);

        assertNotNull(createdUser);
        assertEquals(createdUser.getId(), 1);
        assertEquals(userDto.getName(), createdUser.getName());
        assertEquals(userDto.getEmail(), createdUser.getEmail());

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Transactional
    @Test
    void createUserNotSuccessTest() {
        UserDto userDto = new UserDto(1, "Existing User", "existing.email@example.com");

        when(userRepository.save(Mockito.any(User.class)))
                .thenThrow(new DataIntegrityViolationException("Email already exists"));

        EmailExistsException exception = assertThrows(EmailExistsException.class, () -> {
            userService.createUser(userDto);
        });

        assertEquals("Пользователь с таким email уже существует", exception.getMessage());

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Transactional
    @Test
    void replaceUserUpdateNameSuccessTest() {
        int userId = 1;
        UserDto updatedUserDto = new UserDto(1, "Updated Name", null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(
                new User("Old Name", "old.email@example.com")));

        UserDto replacedUser = userService.replaceUser(updatedUserDto, userId);

        assertNotNull(replacedUser);
        assertEquals("Updated Name", updatedUserDto.getName());
        assertEquals("old.email@example.com", replacedUser.getEmail());

        Mockito.verify(userRepository, Mockito.times(1))
                .saveUserNameById(updatedUserDto.getName(), userId);
        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Transactional
    @Test
    void replaceUserUpdateEmailSuccessTest() {
        int userId = 1;
        UserDto updatedUserDto = new UserDto(1, null, "new.email@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User("Old Name",
                "old.email@example.com")));

        UserDto replacedUser = userService.replaceUser(updatedUserDto, userId);

        assertNotNull(replacedUser);
        assertEquals("Old Name", replacedUser.getName());
        assertEquals("new.email@example.com", updatedUserDto.getEmail());

        Mockito.verify(userRepository, Mockito.times(1))
                .saveUserEmailById(updatedUserDto.getEmail(), userId);
        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Transactional
    @Test
    void replaceUserUpdateNameAndEmailSuccessTest() {
        int userId = 1;
        UserDto userDto = new UserDto(1, "Updated Name", "new.email@example.com");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(new User("Old Name", "old.email@example.com")));

        UserDto updatedUserDto = userService.replaceUser(userDto, userId);

        assertEquals("Old Name", updatedUserDto.getName());
        assertEquals("old.email@example.com", updatedUserDto.getEmail());

        Mockito.verify(userRepository).saveUserById(Mockito.eq("Updated Name"),
                Mockito.eq("new.email@example.com"), Mockito.eq(userId));
    }

    @Transactional
    @Test
    void getUserByIdSuccessTest() {
        int userId = 1;
        User user = new User("John Doe", "john.doe@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());

        Mockito.verify(userRepository).findById(userId);
    }

    @Transactional
    @Test
    void getUserByIdNonExistentUserTest() {
        int userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NonExistentUserException exception = assertThrows(NonExistentUserException.class, () -> {
            userService.getUserById(userId);
        });

        assertEquals("Пользователь с таким id не найден.", exception.getMessage());

        Mockito.verify(userRepository).findById(userId);
    }

    @Transactional
    @Test
    void replaceUserNonExistentUserTest() {
        int userId = 1;
        UserDto updatedUserDto = new UserDto(1, "Updated Name", "new.email@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EmailExistsException exception = assertThrows(EmailExistsException.class, () -> {
            userService.replaceUser(updatedUserDto, userId);
        });

        assertEquals("Пользователь с таким email уже существует", exception.getMessage());

        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
    }

}
