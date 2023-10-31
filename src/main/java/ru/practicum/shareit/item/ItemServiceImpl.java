package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;

    @Override
    public ItemDto createItem(ItemDto itemDto, int userId) {
        return ItemMapper.toItemDto(itemStorage.createItem(itemDto, userId));
    }

    @Override
    public ItemDto replaceItem(ItemDto itemDto, int userId, int itemId) {
        return ItemMapper.toItemDto(itemStorage.replaceItem(itemDto, userId, itemId));
    }

    @Override
    public ItemDto getItemById(int userId, int itemId) {
        return ItemMapper.toItemDto(itemStorage.getItemById(userId, itemId));
    }

    @Override
    public List<ItemDto> getItemsOwnerById(int userId) {
        return itemStorage.getItemsOwnerById(userId);
    }

    @Override
    public List<ItemDto> getItemBySearch(int userId, String text) {
        if (text.isBlank()) {
            return new ArrayList<ItemDto>();
        }
        return itemStorage.getItemBySearch(userId, text);
    }
}