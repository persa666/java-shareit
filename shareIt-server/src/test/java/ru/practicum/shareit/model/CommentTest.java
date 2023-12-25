package ru.practicum.shareit.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest {
    @Test
    public void testDataAnnotation() {
        int testId = 1;
        String testText = "Test Comment Text";
        User testAuthor = new User("Test Author", "test.author@example.com");
        Item testItem =
                new Item(1, testAuthor, "name", "Test Description", true, null);
        LocalDateTime testCreated = LocalDateTime.now();

        Comment comment = new Comment();
        comment.setId(testId);
        comment.setText(testText);
        comment.setItem(testItem);
        comment.setAuthorName(testAuthor);
        comment.setCreated(testCreated);

        assertThat(comment.getId()).isEqualTo(testId);
        assertThat(comment.getText()).isEqualTo(testText);
        assertThat(comment.getItem()).isEqualTo(testItem);
        assertThat(comment.getAuthorName()).isEqualTo(testAuthor);
        assertThat(comment.getCreated()).isEqualTo(testCreated);
    }

    @Test
    public void testAllArgsConstructor() {
        int testId = 1;
        String testText = "Test Comment Text";
        Item testItem = new Item();
        User testAuthor = new User("Test Author", "test.author@example.com");
        LocalDateTime testCreated = LocalDateTime.now();

        Comment comment = new Comment(testId, testText, testItem, testAuthor, testCreated);

        assertThat(comment.getId()).isEqualTo(testId);
        assertThat(comment.getText()).isEqualTo(testText);
        assertThat(comment.getItem()).isEqualTo(testItem);
        assertThat(comment.getAuthorName()).isEqualTo(testAuthor);
        assertThat(comment.getCreated()).isEqualTo(testCreated);
    }

}
