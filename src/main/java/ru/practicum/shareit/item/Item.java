package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private int id;
    private User owner;
    private String name;
    private String description;
    private Boolean available;
    private ItemRequest request;

    public Item(User owner, String name, String description, boolean available, ItemRequest request) {
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }
}
