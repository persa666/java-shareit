package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDtoForSend;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDtoForRequest;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@Rollback
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = {"db.name = h2"})
public class BookingServiceIntegrationTest {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingServiceImpl bookingService;

    @Transactional
    @Test
    void findBookingById() {
        User user = new User("name", "asd@mail.com");
        user = userRepository.save(user);
        User booker = new User("booker", "123@mail.com");
        booker = userRepository.save(booker);
        User nextBooker = new User("booker2", "1234@mail.com");
        nextBooker = userRepository.save(nextBooker);

        ItemDtoForRequest itemDtoForRequest = new ItemDtoForRequest();
        itemDtoForRequest.setName("name");
        itemDtoForRequest.setDescription("desc");
        itemDtoForRequest.setAvailable(true);
        Item item = ItemMapper.toItem(itemDtoForRequest);
        item.setOwner(user);
        item.setRequest(null);
        item = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(Status.APPROVED);

        booking = bookingRepository.save(booking);

        BookingDtoForSend foundBooking = bookingService.findBookingById(booker.getId(), booking.getId());

        assertNotNull(foundBooking, "Найденное бронирование не должно быть null");

        assertEquals(booking.getId(),
                foundBooking.getId(), "ID бронирования должен соответствовать ожидаемому значению");
        assertEquals(booking.getStatus(),
                foundBooking.getStatus(), "Статус бронирования должен соответствовать ожидаемому значению");

        bookingRepository.delete(booking);
    }
}
