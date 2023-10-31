package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, int userId);

    ItemDto replaceItem(ItemDto itemDto, int userId, int itemId);

    ItemDto getItemById(int userId, int itemId);

    List<ItemDto> getItemsOwnerById(int userId);

    List<ItemDto> getItemBySearch(int userId, String text);
}