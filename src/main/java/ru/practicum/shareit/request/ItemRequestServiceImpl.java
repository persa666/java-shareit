package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exp.CountsException;
import ru.practicum.shareit.exp.NonExistentItemRequestException;
import ru.practicum.shareit.exp.NonExistentUserException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForSend;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItem;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
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
        ItemRequest itemRequest = new ItemRequest(0, itemRequestDto.getDescription(), requestor,
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
    public List<ItemRequestDtoWithItem> getAllRequestOtherUsers(int userId, int from, int size) {
        checkFromAndSize(from, size);
        if (!userRepository.existsById(userId)) {
            throw new NonExistentUserException("Пользователя с таким id нет.");
        }
        List<ItemRequestDtoWithItem> allRequests = itemRequestRepository
                .findByRequestorIdNot(userId, PageRequest.of(from / size, size, Sort.by("created")))
                .stream()
                .map(ItemRequestMapper::toItemRequestDtoWithItem)
                .collect(Collectors.toList());
        return getAllRequest(allRequests);
    }

    private List<ItemRequestDtoWithItem> getAllRequest(List<ItemRequestDtoWithItem> list) {
        for (ItemRequestDtoWithItem elem : list) {
            elem.setItems(itemRepository.findByRequestId(elem.getId())
                    .stream()
                    .map(ItemMapper::toItemDtoForRequest)
                    .collect(Collectors.toList()));
        }
        return list;
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

    private void checkFromAndSize(int from, int size) {
        if (from < 0 || size <= 0) {
            throw new CountsException("Параметр(ы) неудовлетворяют условиям пагинации.");
        }
    }
}
