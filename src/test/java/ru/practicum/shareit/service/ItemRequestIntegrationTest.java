package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exp.NonExistentUserException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItem;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Rollback
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = {"db.name = h2"})
public class ItemRequestIntegrationTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRequestServiceImpl itemRequestService;

    @Test
    void getRequestByIdExceptionTest() {
        int userId = 1;
        int requestId = 1;
        assertThrows(NonExistentUserException.class, () -> itemRequestService
                .getRequestById(userId, requestId));
    }

    @Test
    void getRequestByIdTest() {
        User owner = new User("name", "asd@mail.com");
        User requestor = new User("name2", "qwe@mail.com");
        owner = userRepository.save(owner);
        requestor = userRepository.save(requestor);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("desc");
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest = itemRequestRepository.save(itemRequest);

        ItemRequestDtoWithItem resultItemRequest = itemRequestService.getRequestById(requestor.getId(),
                itemRequest.getId());

        assertNotNull(resultItemRequest, "Заявка не должна быть null");
        assertEquals("desc", resultItemRequest.getDescription(), "Описание заявки должно совпадать");
        assertNotNull(resultItemRequest.getCreated(), "Дата создания не должна быть null");
    }
}
