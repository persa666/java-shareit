package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "INSERT INTO comments (text, item_id, author_id, created) VALUES(?2, ?3, ?1, ?4)",
            nativeQuery = true)
    void saveByItemIdAndAuthorNameId(int userId, String text, int itemId, LocalDateTime time);

    Comment findByAuthorNameIdAndItemId(int userId, int itemId);

    @Query("SELECT c FROM Comment AS c WHERE c.item.id = ?1")
    List<Comment> findByItemId(int itemId);
}
