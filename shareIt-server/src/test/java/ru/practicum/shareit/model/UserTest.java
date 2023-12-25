package ru.practicum.shareit.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.User;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    @Test
    public void testGettersAndSetters() {
        String testName = "John Doe";
        String testEmail = "john.doe@example.com";

        User user = new User();

        user.setId(1);
        user.setName(testName);
        user.setEmail(testEmail);

        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getName()).isEqualTo(testName);
        assertThat(user.getEmail()).isEqualTo(testEmail);
    }
}
