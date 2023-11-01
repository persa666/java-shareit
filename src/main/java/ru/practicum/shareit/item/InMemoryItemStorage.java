package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exp.NonExistentItemException;
import ru.practicum.shareit.exp.NonExistentUserException;
import ru.practicum.shareit.user.UserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InMemoryItemStorage implements ItemStorage {
    private int countId = 0;
    private final UserStorage userStorage;
    private final Map<Integer, Item> items = new HashMap<>();

    @Override
    public Item createItem(ItemDto itemDto, int userId) {
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userStorage.getUserById(userId));
        item.setId(++countId);
        items.put(countId, item);
        return item;
    }

    @Override
    public Item replaceItem(ItemDto itemDto, int userId, int itemId) {
        Item oldItem = getItemById(userId, itemId);
        if (oldItem.getOwner().getId() != userId) {
            throw new NonExistentUserException("Пользователь не является владельцем вещи.");
        }
        if (itemDto.getAvailable() != null) {
            oldItem.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getDescription() != null) {
            oldItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getName() != null) {
            oldItem.setName(itemDto.getName());
        }
        items.replace(itemId, oldItem);
        return oldItem;
    }

    @Override
    public Item getItemById(int userId, int itemId) {
        return Optional.ofNullable(items.get(itemId))
                .orElseThrow(() -> new NonExistentItemException("Вещь с таким id не найдена."));
    }

    @Override
    public List<ItemDto> getItemsOwnerById(int userId) {
        return items.values()
                .stream()
                .filter(x -> x.getOwner().getId() == userId)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemBySearch(int userId, String text) {
        return items.values()
                .stream()
                .filter(x -> (x.getName().toLowerCase().contains(text.toLowerCase()) ||
                        x.getDescription().toLowerCase().contains(text.toLowerCase())) && x.getAvailable())
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
