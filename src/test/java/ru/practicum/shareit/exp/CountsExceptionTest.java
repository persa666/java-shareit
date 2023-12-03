package ru.practicum.shareit.exp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountsExceptionTest {
    @Test
    void testCountsException() {
        String errorMessage = "Test error message";
        CountsException countsException = new CountsException(errorMessage);

        assertEquals(errorMessage, countsException.getMessage());
    }
}
