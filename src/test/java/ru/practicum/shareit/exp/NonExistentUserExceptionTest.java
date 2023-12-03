package ru.practicum.shareit.exp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NonExistentUserExceptionTest {
    @Test
    void testCommentException() {
        String errorMessage = "Test error message";
        NonExistentUserException nonExistentUserException = new NonExistentUserException(errorMessage);

        assertEquals(errorMessage, nonExistentUserException.getMessage());
    }
}
