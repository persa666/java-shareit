package ru.practicum.shareit.item;

public class CommentMapper {

    public static CommentDtoForSend toCommentDtoForSend(Comment comment) {
        return new CommentDtoForSend(
                comment.getId(),
                comment.getText(),
                comment.getAuthorName().getName(),
                comment.getCreated()
        );
    }
}
