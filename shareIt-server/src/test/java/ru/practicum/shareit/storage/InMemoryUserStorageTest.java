package ru.practicum.shareit.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exp.EmailExistsException;
import ru.practicum.shareit.exp.NonExistentUserException;
import ru.practicum.shareit.user.InMemoryUserStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryUserStorageTest {
    private InMemoryUserStorage userStorage;

    @BeforeEach
    void setUp() {
        userStorage = new InMemoryUserStorage();
    }

    @Test
    void testReplaceUserWithEmailChange() {
        UserDto newUserDto = new UserDto(1, null, "newemail@example.com");
        int userId = 1;
        User oldUser = new User(userId, "OldName", "oldemail@example.com");

        userStorage.createUser(oldUser);
        User result = userStorage.replaceUser(newUserDto, userId);

        assertNotNull(result);

        assertEquals(oldUser.getName(), result.getName());

        assertEquals(newUserDto.getEmail(), result.getEmail());

        assertEquals(oldUser, userStorage.getUserById(userId));
    }

    @Test
    void testReplaceUserWithEqualsEmailChange() {
        UserDto newUserDto = new UserDto(1, "newName", "oldemail@example.com");
        int userId = 1;
        User oldUser = new User(userId, "OldName", "oldemail@example.com");

        userStorage.createUser(oldUser);
        User result = userStorage.replaceUser(newUserDto, userId);

        assertNotNull(result);

        assertEquals(oldUser.getName(), result.getName());

        assertEquals(newUserDto.getEmail(), result.getEmail());

        assertEquals(oldUser, userStorage.getUserById(userId));
    }

    @Test
    void testReplaceUserWithNameChange() {
        UserDto newUserDto = new UserDto(1, "NewName", null);
        int userId = 1;
        User oldUser = new User(userId, "OldName", "oldemail@example.com");

        userStorage.createUser(oldUser);

        User result = userStorage.replaceUser(newUserDto, userId);

        assertNotNull(result);
        assertEquals(newUserDto.getName(), result.getName());
        assertEquals(oldUser.getEmail(), result.getEmail());

        assertEquals(result, userStorage.getUserById(userId));
    }

    @Test
    void testReplaceUserWithNoChange() {
        UserDto newUserDto = new UserDto(1, null, null);
        int userId = 1;
        User oldUser = new User(userId, "OldName", "oldemail@example.com");

        userStorage.createUser(oldUser);

        User result = userStorage.replaceUser(newUserDto, userId);

        assertNotNull(result);
        assertEquals(oldUser.getName(), result.getName());
        assertEquals(oldUser.getEmail(), result.getEmail());

        assertEquals(oldUser, userStorage.getUserById(userId));
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User(1, "User1", "user1@example.com");
        User user2 = new User(2, "User2", "user2@example.com");
        userStorage.createUser(user1);
        userStorage.createUser(user2);

        var allUsers = userStorage.getAllUsers();

        assertEquals(2, allUsers.size());

        assertTrue(allUsers.contains(user1));
        assertTrue(allUsers.contains(user2));
    }

    @Test
    void testCreateUser() {
        UserDto newUserDto = new UserDto(0, "NewUser", "newuser@example.com");

        User result = userStorage.createUser(new User(newUserDto.getName(), newUserDto.getEmail()));

        assertNotNull(result);
        assertTrue(result.getId() > 0);

        assertEquals(result, userStorage.getUserById(result.getId()));
    }

    @Test
    void testGetUserById() {
        User user = new User(1, "TestUser", "testuser@example.com");
        userStorage.createUser(user);

        User result = userStorage.getUserById(1);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testDeleteUser() {
        User user = new User(1, "TestUser", "testuser@example.com");
        userStorage.createUser(user);

        userStorage.deleteUser(1);

        assertThrows(NonExistentUserException.class, () -> userStorage.getUserById(1));
    }

    @Test
    void testCheckExistEmail() {
        User user = new User(1, "TestUser", "testuser@example.com");
        userStorage.createUser(user);

        UserDto newUserDto = new UserDto(0, "NewUser", "testuser@example.com");
        assertThrows(EmailExistsException.class,
                () -> userStorage.createUser(new User(newUserDto.getName(), newUserDto.getEmail())));
    }
}
