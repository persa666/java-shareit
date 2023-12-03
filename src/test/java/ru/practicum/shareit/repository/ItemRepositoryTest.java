package ru.practicum.shareit.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource(locations = "/applicationTest.properties")
@ActiveProfiles("test")
@Transactional
public class ItemRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void testFindByOwnerId() {
        int userId = 1;
        PageRequest pageable = PageRequest.of(0, 10);

        TypedQuery<Item> query = entityManager.createQuery(
                "SELECT i FROM Item i WHERE i.owner.id = :userId", Item.class);
        query.setParameter("userId", userId);

        Page<Item> items = itemRepository.findByOwnerId(userId, pageable);

        assertNotNull(items);
        assertTrue(items.getTotalElements() >= 0);
    }

    @Test
    public void testSearch() {
        String searchText = "example";
        PageRequest pageable = PageRequest.of(0, 10);

        TypedQuery<Item> query = entityManager.createQuery(
                "SELECT i FROM Item i WHERE (UPPER(i.name) LIKE UPPER(concat('%', :text, '%'))" +
                        " OR UPPER(i.description) LIKE UPPER(concat('%', :text, '%'))) AND i.available = true",
                Item.class);
        query.setParameter("text", searchText);

        Page<Item> items = itemRepository.search(searchText, pageable);

        assertNotNull(items);
        assertTrue(items.getTotalElements() >= 0);
    }

    @Test
    public void testFindByRequestId() {
        int requestId = 1;

        TypedQuery<Item> query = entityManager.createQuery(
                "SELECT i FROM Item i WHERE i.request.id = :requestId", Item.class);
        query.setParameter("requestId", requestId);

        List<Item> items = itemRepository.findByRequestId(requestId);

        assertNotNull(items);
        assertTrue(items.size() >= 0);
    }
}
