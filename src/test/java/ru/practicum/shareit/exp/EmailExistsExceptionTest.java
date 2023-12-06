package ru.practicum.shareit.exp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmailExistsExceptionTest {
    @Test
    void testEmailExistsException() {
        String errorMessage = "Test error message";
        EmailExistsException emailExistsException = new EmailExistsException(errorMessage);

        assertEquals(errorMessage, emailExistsException.getMessage());
    }
}
