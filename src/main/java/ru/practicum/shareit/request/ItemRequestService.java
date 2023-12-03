package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForSend;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItem;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDtoForSend createRequest(int userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDtoWithItem> getAllRequestsUser(int userId);

    List<ItemRequestDtoWithItem> getAllRequestOtherUsers(int userId, int from, int size);

    ItemRequestDtoWithItem getRequestById(int userId, int requestId);
}
