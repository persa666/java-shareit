package ru.practicum.shareit.exp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusExceptionTest {
    @Test
    void testCommentException() {
        String errorMessage = "Test error message";
        StatusException statusException = new StatusException(errorMessage);

        assertEquals(errorMessage, statusException.getMessage());
    }
}
