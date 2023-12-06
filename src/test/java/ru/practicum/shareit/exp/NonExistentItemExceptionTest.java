package ru.practicum.shareit.exp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NonExistentItemExceptionTest {
    @Test
    void testNonExistentItemException() {
        String errorMessage = "Test error message";
        NonExistentItemException nonExistentItemException = new NonExistentItemException(errorMessage);

        assertEquals(errorMessage, nonExistentItemException.getMessage());
    }
}
