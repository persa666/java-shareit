package ru.practicum.shareit.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.CommentDtoForSend;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentDtoForSendTest {
    @Test
    public void testDataAnnotation() {
        int testId = 1;
        String testText = "Test Comment Text";
        String testAuthorName = "Test Author";
        LocalDateTime testCreated = LocalDateTime.now();

        CommentDtoForSend commentDto = new CommentDtoForSend();
        commentDto.setId(testId);
        commentDto.setText(testText);
        commentDto.setAuthorName(testAuthorName);
        commentDto.setCreated(testCreated);

        assertThat(commentDto.getId()).isEqualTo(testId);
        assertThat(commentDto.getText()).isEqualTo(testText);
        assertThat(commentDto.getAuthorName()).isEqualTo(testAuthorName);
        assertThat(commentDto.getCreated()).isEqualTo(testCreated);
    }
}
