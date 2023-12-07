package ru.practicum.shareit.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDtoForSend;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItem;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ItemRequestMapperTest {
    @Test
    public void testToItemRequestDtoForSend() {
        ItemRequestMapper itemRequestMapper = new ItemRequestMapper();
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);
        itemRequest.setDescription("Test Request");
        itemRequest.setCreated(LocalDateTime.now());

        ItemRequestDtoForSend dtoForSend = ItemRequestMapper.toItemRequestDtoForSend(itemRequest);
        assertEquals(1, dtoForSend.getId());
        assertEquals("Test Request", dtoForSend.getDescription());
        assertEquals(itemRequest.getCreated(), dtoForSend.getCreated());
    }

    @Test
    public void testToItemRequestDtoWithItem() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);
        itemRequest.setDescription("Test Request");
        itemRequest.setCreated(LocalDateTime.now());

        ItemRequestDtoWithItem dtoWithItem = ItemRequestMapper.toItemRequestDtoWithItem(itemRequest);
        assertEquals(1, dtoWithItem.getId());
        assertEquals("Test Request", dtoWithItem.getDescription());
        assertEquals(itemRequest.getCreated(), dtoWithItem.getCreated());
        assertNotNull(dtoWithItem.getItems());
        assertTrue(dtoWithItem.getItems().isEmpty());
    }
}
