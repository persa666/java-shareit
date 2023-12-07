package ru.practicum.shareit.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.CommentDtoForSend;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentMapperTest {
    @Test
    void testToCommentDtoForSend() {
        CommentMapper commentMapper = new CommentMapper();
        Comment comment = new Comment(1, "Test Comment", new Item(), new User(), LocalDateTime.now());

        CommentDtoForSend commentDtoForSend = CommentMapper.toCommentDtoForSend(comment);

        assertEquals(comment.getId(), commentDtoForSend.getId());
        assertEquals(comment.getText(), commentDtoForSend.getText());
        assertEquals(comment.getAuthorName().getName(), commentDtoForSend.getAuthorName());
        assertEquals(comment.getCreated(), commentDtoForSend.getCreated());
    }
}
