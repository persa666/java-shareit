package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@Rollback
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = {"db.name = h2"})
public class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Test
    public void testGetAllUsers() {
        User user1 = new User("John", "john@example.com");
        User user2 = new User("Jane", "jane@example.com");

        userRepository.save(user1);
        userRepository.save(user2);

        List<UserDto> userDtos = userService.getAllUsers();

        assertTrue(userDtos.size() > 0);
        assertEquals(2, userDtos.size());

        assertEquals("John", userDtos.get(0).getName());
        assertEquals("john@example.com", userDtos.get(0).getEmail());
        assertEquals("Jane", userDtos.get(1).getName());
        assertEquals("jane@example.com", userDtos.get(1).getEmail());

        userRepository.delete(user2);
        userRepository.delete(user1);
    }
}
