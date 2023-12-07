package ru.practicum.shareit.item;

import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ItemService {
    ItemDtoForRequest createItem(ItemDtoForRequest itemDtoForRequest, int userId);

    ItemDto replaceItem(ItemDto itemDto, int userId, int itemId);

    ItemBookingDto getItemById(int userId, int itemId);

    List<ItemBookingDto> getItemsOwnerById(int userId, PageRequest pageRequest);

    List<ItemDto> getItemBySearch(int userId, String text, PageRequest pageRequest);

    CommentDtoForSend createComment(int userId, CommentDto text, int itemId);
}