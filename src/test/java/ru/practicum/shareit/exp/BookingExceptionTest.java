package ru.practicum.shareit.exp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingExceptionTest {
    @Test
    void testBookingException() {
        String errorMessage = "Test error message";
        BookingException bookingException = new BookingException(errorMessage);

        assertEquals(errorMessage, bookingException.getMessage());
    }
}
