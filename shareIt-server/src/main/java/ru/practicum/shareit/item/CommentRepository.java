package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Comment findByAuthorNameIdAndItemId(int userId, int itemId);

    @Query("SELECT c FROM Comment AS c WHERE c.item.id = ?1")
    List<Comment> findByItemId(int itemId);
}
