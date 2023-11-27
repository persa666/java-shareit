package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findAllByOwner_Id(@Param("owner_id") int userId);

    @Query("SELECT i FROM Item AS i WHERE (UPPER(i.name) LIKE UPPER(concat('%', ?1, '%'))" +
            "OR UPPER(i.description) LIKE UPPER(concat('%', ?1, '%'))) AND i.available = true")
    List<Item> search(String text);

    @Query("SELECT i.available from Item AS i WHERE i.id = ?1")
    Boolean findAvailableById(@Param("id") int itemId);
}
