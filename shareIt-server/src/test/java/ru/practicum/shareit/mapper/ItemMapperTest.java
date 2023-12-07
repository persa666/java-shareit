package ru.practicum.shareit.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ItemMapperTest {
    @Test
    void testToItemDto() {
        Item item = new Item(1, new User(), "TestDescription", "true", true, null);
        ItemMapper itemMapper = new ItemMapper();
        ItemDto itemDto = ItemMapper.toItemDto(item);

        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertEquals(item.getAvailable(), itemDto.getAvailable());
    }

    @Test
    void testToItem() {
        ItemDto itemDto = new ItemDto(1, "TestItem", "TestDescription", true);

        Item item = ItemMapper.toItem(itemDto);

        assertEquals(itemDto.getId(), item.getId());
        assertNull(item.getOwner());
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
        assertEquals(itemDto.getAvailable(), item.getAvailable());
        assertNull(item.getRequest());
    }

    @Test
    void testToItemForRequest() {
        ItemDtoForRequest itemDtoForRequest =
                new ItemDtoForRequest(1, "TestItem", "TestDescription", true, 123);

        Item item = ItemMapper.toItem(itemDtoForRequest);

        assertEquals(itemDtoForRequest.getId(), item.getId());
        assertNull(item.getOwner());
        assertEquals(itemDtoForRequest.getName(), item.getName());
        assertEquals(itemDtoForRequest.getDescription(), item.getDescription());
        assertEquals(itemDtoForRequest.getAvailable(), item.getAvailable());
        assertNull(item.getRequest());
    }

    @Test
    void testToItemBookingDto() {
        Item item = new Item(1, new User(), "TestDescription", "true", true, null);

        ItemBookingDto itemBookingDto = ItemMapper.toItemBookingDto(item);

        assertEquals(item.getId(), itemBookingDto.getId());
        assertEquals(item.getName(), itemBookingDto.getName());
        assertEquals(item.getDescription(), itemBookingDto.getDescription());
        assertEquals(item.getAvailable(), itemBookingDto.getAvailable());
        assertNull(itemBookingDto.getRequest());
        assertNull(itemBookingDto.getLastBooking());
        assertNull(itemBookingDto.getNextBooking());
        assertNull(itemBookingDto.getComments());
    }

    @Test
    void testToItemDtoForRequest() {
        Item item = new Item(1, new User(), "TestDescription", "true", true, null);

        ItemDtoForRequest itemDtoForRequest = ItemMapper.toItemDtoForRequest(item);

        assertEquals(item.getId(), itemDtoForRequest.getId());
        assertEquals(item.getName(), itemDtoForRequest.getName());
        assertEquals(item.getDescription(), itemDtoForRequest.getDescription());
        assertEquals(item.getAvailable(), itemDtoForRequest.getAvailable());
        assertEquals(item.getRequestIdOrNull(), itemDtoForRequest.getRequestId());
    }
}
