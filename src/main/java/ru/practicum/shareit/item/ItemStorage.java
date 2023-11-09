package ru.practicum.shareit.item;

import java.util.List;

public interface ItemStorage {
    Item createItem(ItemDto itemDto, int userId);

    Item replaceItem(ItemDto itemDto, int userId, int itemId);

    Item getItemById(int userId, int itemId);

    List<ItemDto> getItemsOwnerById(int userId);

    List<ItemDto> getItemBySearch(int userId, String text);
}
