package ru.practicum.shareit.exp;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exp.NonExistentBookingException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NonExistentBookingExceptionTest {
    @Test
    void testNonExistentBookingException() {
        String errorMessage = "Test error message";
        NonExistentBookingException nonExistentBookingException = new NonExistentBookingException(errorMessage);

        assertEquals(errorMessage, nonExistentBookingException.getMessage());
    }
}
