package ru.practicum.shareit.exp;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exp.NonExistentItemRequestException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NonExistentItemRequestExceptionTest {
    @Test
    void testCommentException() {
        String errorMessage = "Test error message";
        NonExistentItemRequestException nonExistentItemRequestException = new NonExistentItemRequestException(errorMessage);

        assertEquals(errorMessage, nonExistentItemRequestException.getMessage());
    }
}
