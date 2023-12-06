package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDtoForSend;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItem;

import java.util.ArrayList;

public class ItemRequestMapper {
    public static ItemRequestDtoForSend toItemRequestDtoForSend(ItemRequest itemRequest) {
        return new ItemRequestDtoForSend(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated()
        );
    }

    public static ItemRequestDtoWithItem toItemRequestDtoWithItem(ItemRequest itemRequest) {
        return new ItemRequestDtoWithItem(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                new ArrayList<>()
        );
    }
}
