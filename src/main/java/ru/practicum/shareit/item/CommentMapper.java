package ru.practicum.shareit.item;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comment toComment(CommentDto commentDto) {
        return new Comment(
                0,
                commentDto.getText(),
                null,
                null,
                LocalDateTime.now()
        );
    }

    public static CommentDtoForSend toCommentDtoForSend(Comment comment) {
        return new CommentDtoForSend(
                comment.getId(),
                comment.getText(),
                comment.getAuthorName().getName(),
                comment.getCreated()
        );
    }
}
