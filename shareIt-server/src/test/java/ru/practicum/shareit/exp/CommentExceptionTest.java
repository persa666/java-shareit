package ru.practicum.shareit.exp;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exp.CommentException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentExceptionTest {
    @Test
    void testCommentException() {
        String errorMessage = "Test error message";
        CommentException commentException = new CommentException(errorMessage);

        assertEquals(errorMessage, commentException.getMessage());
    }
}
