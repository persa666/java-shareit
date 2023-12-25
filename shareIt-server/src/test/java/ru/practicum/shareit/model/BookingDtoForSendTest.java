package ru.practicum.shareit.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDtoForSend;
import ru.practicum.shareit.item.ItemDtoForBooking;
import ru.practicum.shareit.user.UserDtoForBooking;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingDtoForSendTest {
    @Test
    public void testGetterSetter() {
        int testId = 1;
        LocalDateTime testStart = LocalDateTime.now();
        LocalDateTime testEnd = testStart.plusHours(2);
        Status testStatus = Status.WAITING;
        UserDtoForBooking testBooker = new UserDtoForBooking();
        ItemDtoForBooking testItem = new ItemDtoForBooking();

        BookingDtoForSend bookingDto = new BookingDtoForSend();
        bookingDto.setId(testId);
        bookingDto.setStart(testStart);
        bookingDto.setEnd(testEnd);
        bookingDto.setStatus(testStatus);
        bookingDto.setBooker(testBooker);
        bookingDto.setItem(testItem);

        assertThat(bookingDto.getId()).isEqualTo(testId);
        assertThat(bookingDto.getStart()).isEqualTo(testStart);
        assertThat(bookingDto.getEnd()).isEqualTo(testEnd);
        assertThat(bookingDto.getStatus()).isEqualTo(testStatus);
        assertThat(bookingDto.getBooker()).isEqualTo(testBooker);
        assertThat(bookingDto.getItem()).isEqualTo(testItem);
    }
}
