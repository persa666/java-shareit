package ru.practicum.shareit.exp;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exp.ErrorResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorResponseTest {
    @Test
    void testErrorResponse() {
        String error = "TestError";
        String description = "TestDescription";

        ErrorResponse errorResponse = new ErrorResponse(error, description);

        assertEquals(error, errorResponse.getError());
        assertEquals(description, errorResponse.getDescription());
    }
}
