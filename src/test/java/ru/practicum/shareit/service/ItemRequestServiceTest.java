package ru.practicum.shareit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exp.NonExistentUserException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForSend;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItem;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Transactional
    @Test
    void createRequestTest() {
        int userId = 1;
        User requestor = new User(1, "name", "123@mail.com");
        ItemRequestDto itemRequestDto = new ItemRequestDto("desc");
        ItemRequest itemRequest = new ItemRequest(0, itemRequestDto.getDescription(), requestor,
                LocalDateTime.now());
        when(userRepository.findById(userId)).thenReturn(Optional.of(requestor));
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDtoForSend itemRequestDtoForSend = itemRequestService.createRequest(userId, itemRequestDto);

        assertNotNull(itemRequestDtoForSend, "Созданный объект не должен быть null");
        assertEquals(itemRequest.getId(), itemRequestDtoForSend.getId(), "ID заявки должен совпадать");
        assertEquals(itemRequestDto.getDescription(), itemRequestDtoForSend.getDescription(),
                "Описание заявки должно совпадать");
        assertNotNull(itemRequestDtoForSend.getCreated(), "Дата создания не должна быть null");

        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        Mockito.verify(itemRequestRepository, Mockito.times(1)).save(any(ItemRequest.class));
    }

    @Transactional
    @Test
    void getAllRequestsUserExceptionTest() {
        int userId = 1;
        when(userRepository.existsById(any(Integer.class))).thenReturn(false);

        assertThrows(NonExistentUserException.class, () -> itemRequestService.getAllRequestsUser(userId));

        Mockito.verify(userRepository, Mockito.times(1)).existsById(userId);
    }

    @Transactional
    @Test
    void getAllRequestsUserTest() {
        int userId = 1;
        User user = new User(1, "name", "123@mail.com");
        List<ItemRequest> list = List.of(
                new ItemRequest(1, "name", user, LocalDateTime.now())
        );
        List<Item> items = new ArrayList<>();

        when(userRepository.existsById(any(Integer.class))).thenReturn(true);
        when(itemRequestRepository.findByRequestorId(userId)).thenReturn(list);
        when(itemRepository.findByRequestIdIn(any(List.class))).thenReturn(items);

        List<ItemRequestDtoWithItem> requests = itemRequestService.getAllRequestsUser(userId);

        assertNotNull(requests, "Список запросов не должен быть null");
        assertFalse(requests.isEmpty(), "Список запросов не должен быть пустым");

        Mockito.verify(userRepository, Mockito.times(1)).existsById(userId);
        Mockito.verify(itemRequestRepository, Mockito.times(1)).findByRequestorId(userId);
    }

    @Transactional
    @Test
    void getAllRequestOtherUsersExceptionTest() {
        int userId = 1;
        int from = 0;
        int size = 1;

        when(userRepository.existsById(any(Integer.class))).thenReturn(false);

        assertThrows(NonExistentUserException.class, () -> itemRequestService
                .getAllRequestOtherUsers(userId, PageRequest.of(from / size, size, Sort.by("created"))));

        Mockito.verify(userRepository, Mockito.times(1)).existsById(userId);
    }

    @Transactional
    @Test
    void getAllRequestOtherUsersTest() {
        int userId = 1;
        int from = 0;
        int size = 1;
        List<ItemRequest> list = new ArrayList<>();
        Page<ItemRequest> page = new PageImpl<>(list);

        when(userRepository.existsById(any(Integer.class))).thenReturn(true);
        when(itemRequestRepository.findByRequestorIdNot(any(Integer.class), any(Pageable.class))).thenReturn(page);

        List<ItemRequestDtoWithItem> requests = itemRequestService.getAllRequestOtherUsers(userId,
                PageRequest.of(from / size, size, Sort.by("created")));

        assertTrue(requests.isEmpty(), "Список должен быть пустым");
        assertEquals(0, requests.size(), "Список должен содержать 0 элементов");

        Mockito.verify(userRepository, Mockito.times(1)).existsById(userId);
        Mockito.verify(itemRequestRepository, Mockito.times(1))
                .findByRequestorIdNot(userId, PageRequest.of(from, size, Sort.by("created")));
    }
}
