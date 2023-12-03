package ru.practicum.shareit.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForSend;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingMapperTest {
    @Test
    public void testToBookingForSend() {
        BookingMapper bookingMapper = new BookingMapper();
        Booking booking = new Booking();
        booking.setId(1);
        booking.setStart(LocalDateTime.of(2020, 10, 10, 10, 10));
        booking.setEnd(LocalDateTime.of(2020, 10, 10, 10, 20));
        booking.setStatus(Status.WAITING);

        User booker = new User();
        booker.setId(1);
        booking.setBooker(booker);

        Item item = new Item();
        item.setId(2);
        item.setName("Test Item");
        booking.setItem(item);

        BookingDtoForSend bookingDtoForSend = BookingMapper.toBookingForSend(booking);
        assertEquals(1L, bookingDtoForSend.getId());
        assertEquals(LocalDateTime
                .of(2020, 10, 10, 10, 10), bookingDtoForSend.getStart());
        assertEquals(LocalDateTime
                .of(2020, 10, 10, 10, 20), bookingDtoForSend.getEnd());
        assertEquals(Status.WAITING, bookingDtoForSend.getStatus());
        assertEquals(1, bookingDtoForSend.getBooker().getId());
        assertEquals(2, bookingDtoForSend.getItem().getId());
        assertEquals("Test Item", bookingDtoForSend.getItem().getName());
    }

    @Test
    public void testToBookingDto() {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.of(2020, 10, 10, 10, 10));
        booking.setEnd(LocalDateTime.of(2020, 10, 10, 10, 20));

        Item item = new Item();
        item.setId(1);
        booking.setItem(item);

        // Вызываем метод toBookingDto и проверяем ожидаемые значения
        BookingDto bookingDto = BookingMapper.toBookingDto(booking);
        assertEquals(1, bookingDto.getItemId());
        assertEquals(LocalDateTime.of(2020, 10, 10, 10, 10), bookingDto.getStart());
        assertEquals(LocalDateTime.of(2020, 10, 10, 10, 20), bookingDto.getEnd());
    }
}
