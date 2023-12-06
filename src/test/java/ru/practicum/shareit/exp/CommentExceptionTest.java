package ru.practicum.shareit.exp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentExceptionTest {
    @Test
    void testCommentException() {
        String errorMessage = "Test error message";
        CommentException commentException = new CommentException(errorMessage);

        assertEquals(errorMessage, commentException.getMessage());
    }
}
