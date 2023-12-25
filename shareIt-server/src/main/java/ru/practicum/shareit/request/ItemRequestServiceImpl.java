package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exp.NonExistentItemRequestException;
import ru.practicum.shareit.exp.NonExistentUserException;
import ru.practicum.shareit.item.ItemDtoForRequest;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForSend;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItem;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDtoForSend createRequest(int userId, ItemRequestDto itemRequestDto) {
        User requestor = userRepository.findById(userId)
                .orElseThrow(() -> new NonExistentUserException("Пользователя с таким id нет"));
        ItemRequest itemRequest = new ItemRequest(itemRequestDto.getDescription(), requestor,
                LocalDateTime.now());
        return ItemRequestMapper.toItemRequestDtoForSend(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDtoWithItem> getAllRequestsUser(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new NonExistentUserException("Пользователя с таким id нет.");
        }
        List<ItemRequestDtoWithItem> allRequests = itemRequestRepository.findByRequestorId(userId)
                .stream()
                .map(ItemRequestMapper::toItemRequestDtoWithItem)
                .collect(Collectors.toList());
        return getAllRequest(allRequests);
    }

    @Override
    public List<ItemRequestDtoWithItem> getAllRequestOtherUsers(int userId, PageRequest pageRequest) {
        if (!userRepository.existsById(userId)) {
            throw new NonExistentUserException("Пользователя с таким id нет.");
        }
        List<ItemRequestDtoWithItem> allRequests = itemRequestRepository
                .findByRequestorIdNot(userId, pageRequest)
                .stream()
                .map(ItemRequestMapper::toItemRequestDtoWithItem)
                .collect(Collectors.toList());
        return getAllRequest(allRequests);
    }

    private List<ItemRequestDtoWithItem> getAllRequest(List<ItemRequestDtoWithItem> list) {
        List<Integer> requestIds = list.stream()
                .map(ItemRequestDtoWithItem::getId)
                .collect(Collectors.toList());
        List<ItemDtoForRequest> items = itemRepository.findByRequestIdIn(requestIds)
                .stream()
                .map(ItemMapper::toItemDtoForRequest)
                .collect(Collectors.toList());
        Map<Integer, List<ItemDtoForRequest>> itemsByRequestId = items.stream()
                .collect(Collectors.groupingBy(ItemDtoForRequest::getRequestId));
        return list.stream()
                .peek(request -> {
                    List<ItemDtoForRequest> associatedItems = itemsByRequestId.getOrDefault(request.getId(),
                            Collections.emptyList());
                    request.setItems(associatedItems);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDtoWithItem getRequestById(int userId, int requestId) {
        if (!userRepository.existsById(userId)) {
            throw new NonExistentUserException("Пользователя с таким id нет.");
        }
        ItemRequestDtoWithItem request = ItemRequestMapper.toItemRequestDtoWithItem(itemRequestRepository
                .findById(requestId).orElseThrow(() -> new NonExistentItemRequestException("Запроса с таким id нет.")));
        request.setItems(itemRepository.findByRequestId(requestId)
                .stream()
                .map(ItemMapper::toItemDtoForRequest)
                .collect(Collectors.toList()));
        return request;
    }
}
