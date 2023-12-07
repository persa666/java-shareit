package ru.practicum.shareit.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.InMemoryItemStorage;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InMemoryItemStorageTest {

    @Mock
    private UserStorage userStorage;

    @InjectMocks
    private InMemoryItemStorage itemStorage;

    @Test
    void testCreateItem() {
        User user = new User(1, "John Doe", "john@example.com");
        when(userStorage.getUserById(1)).thenReturn(user);

        ItemDto itemDto = new ItemDto(1, "Test Item", "Description", true);
        Item createdItem = itemStorage.createItem(itemDto, 1);

        assertNotNull(createdItem);
        assertEquals("Test Item", createdItem.getName());
        assertEquals("Description", createdItem.getDescription());
        assertEquals(user, createdItem.getOwner());
        assertTrue(createdItem.getAvailable());
    }

    @Test
    void testReplaceItem() {
        User user = new User(1, "John Doe", "john@example.com");
        when(userStorage.getUserById(1)).thenReturn(user);


        ItemDto itemDto = new ItemDto(1, "New Item", "New Description", false);
        itemStorage.createItem(itemDto, 1);
        ItemDto newItemDto = new ItemDto(1, "New Item", "New", false);
        Item replacedItem = itemStorage.replaceItem(newItemDto, 1, 1);

        assertNotNull(replacedItem);
        assertEquals("New Item", replacedItem.getName());
        assertEquals("New", replacedItem.getDescription());
        assertEquals(false, replacedItem.getAvailable());
        assertEquals(user, replacedItem.getOwner());
    }

    @Test
    void testGetItemsOwnerById() {
        User user = new User(1, "John Doe", "john@example.com");
        when(userStorage.getUserById(1)).thenReturn(user);

        itemStorage.createItem(new ItemDto(1, "Item 1", "Description 1", true), 1);
        itemStorage.createItem(new ItemDto(2, "Item 2", "Description 2", false), 1);

        List<ItemDto> items = itemStorage.getItemsOwnerById(1);

        assertEquals(2, items.size());
        assertEquals("Item 1", items.get(0).getName());
        assertEquals("Item 2", items.get(1).getName());
    }

    @Test
    void testGetItemBySearch() {
        User user = new User(1, "John Doe", "john@example.com");
        when(userStorage.getUserById(1)).thenReturn(user);

        itemStorage.createItem(new ItemDto(1, "Item 1", "Description 1", true), 1);
        itemStorage.createItem(new ItemDto(3, "Another Item", "Another Description", true), 1);


        List<ItemDto> items = itemStorage.getItemBySearch(1, "item");

        assertEquals(2, items.size()); // Ожидаем, что найдены два элемента
        assertTrue(items.stream().anyMatch(item -> item.getName().equals("Item 1")));
    }
}