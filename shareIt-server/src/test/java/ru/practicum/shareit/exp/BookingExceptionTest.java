package ru.practicum.shareit.exp;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exp.BookingException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingExceptionTest {
    @Test
    void testBookingException() {
        String errorMessage = "Test error message";
        BookingException bookingException = new BookingException(errorMessage);

        assertEquals(errorMessage, bookingException.getMessage());
    }
}
