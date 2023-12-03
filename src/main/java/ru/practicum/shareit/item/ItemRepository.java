package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    Page<Item> findByOwnerId(int userId, Pageable pageable);

    @Query("SELECT i FROM Item AS i WHERE (UPPER(i.name) LIKE UPPER(concat('%', :text, '%'))" +
            "OR UPPER(i.description) LIKE UPPER(concat('%', :text, '%'))) AND i.available = true")
    Page<Item> search(@Param("text") String text, Pageable pageable);

    List<Item> findByRequestId(int requestId);
}
