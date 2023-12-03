package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    ItemDtoForRequest createItem(ItemDtoForRequest itemDtoForRequest, int userId);

    ItemDto replaceItem(ItemDto itemDto, int userId, int itemId);

    ItemBookingDto getItemById(int userId, int itemId);

    List<ItemBookingDto> getItemsOwnerById(int userId, int from, int size);

    List<ItemDto> getItemBySearch(int userId, String text, int from, int size);

    CommentDtoForSend createComment(int userId, CommentDto text, int itemId);
}