package ru.practicum.shareit.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.UserDtoForBooking;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDtoForBookingTest {
    @Test
    public void testDataAnnotation() {
        int testId = 1;

        UserDtoForBooking userDto = new UserDtoForBooking();
        userDto.setId(testId);

        assertThat(userDto.getId()).isEqualTo(testId);
    }
}
