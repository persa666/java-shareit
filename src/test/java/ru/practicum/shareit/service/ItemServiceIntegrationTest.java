package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@Rollback
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = {"db.name = h2"})
public class ItemServiceIntegrationTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private ItemServiceImpl itemService;

    @Test
    void getItemsOwnerByIdTest() {
        User user = new User("name", "asd@mail.com");
        user = userRepository.save(user);
        User booker = new User("booker", "123@mail.com");
        booker = userRepository.save(booker);
        User nextBooker = new User("booker2", "1234@mail.com");
        nextBooker = userRepository.save(nextBooker);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("des");
        itemRequest.setRequestor(booker);
        itemRequest.setCreated(LocalDateTime.now());
        CommentDto commentDto = new CommentDto("text");

        itemRequest = itemRequestRepository.save(itemRequest);
        ItemDtoForRequest itemDtoForRequest = new ItemDtoForRequest();
        itemDtoForRequest.setName("name");
        itemDtoForRequest.setDescription("desc");
        itemDtoForRequest.setAvailable(true);
        itemDtoForRequest.setRequestId(itemRequest.getId());
        Item item = ItemMapper.toItem(itemDtoForRequest);
        item.setOwner(user);
        item.setRequest(itemRequest);
        item = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(Status.APPROVED);

        Booking nextBooking = new Booking();
        nextBooking.setId(2);
        nextBooking.setStart(LocalDateTime.now().plusDays(1));
        nextBooking.setEnd(LocalDateTime.now().plusDays(2));
        nextBooking.setItem(item);
        nextBooking.setBooker(nextBooker);
        nextBooking.setStatus(Status.APPROVED);

        itemService.createItem(itemDtoForRequest, user.getId());
        booking = bookingRepository.save(booking);
        nextBooking = bookingRepository.save(nextBooking);
        itemService.createComment(booker.getId(), commentDto, item.getId());
        List<ItemBookingDto> list = itemService.getItemsOwnerById(user.getId(), 0, 1);

        assertNotNull(list, "Список не должен быть null");
        assertEquals(1, list.size(), "Размер списка должен быть равен 1");
    }
}
