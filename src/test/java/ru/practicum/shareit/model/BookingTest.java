package ru.practicum.shareit.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingTest {
    @Test
    public void testGetterAndSetter() {
        Booking booking = new Booking();
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusHours(2);
        Item item = new Item();
        User booker = new User();
        Status status = Status.WAITING;

        booking.setId(1);
        booking.setStart(startDate);
        booking.setEnd(endDate);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(status);

        assertThat(booking.getId()).isEqualTo(1);
        assertThat(booking.getStart()).isEqualTo(startDate);
        assertThat(booking.getEnd()).isEqualTo(endDate);
        assertThat(booking.getItem()).isEqualTo(item);
        assertThat(booking.getBooker()).isEqualTo(booker);
        assertThat(booking.getStatus()).isEqualTo(status);
    }

    @Test
    public void testAllArgsConstructor() {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusHours(2);
        Item item = new Item();
        User booker = new User();
        Status status = Status.WAITING;

        Booking booking = new Booking(1, startDate, endDate, item, booker, status);

        assertThat(booking.getId()).isEqualTo(1);
        assertThat(booking.getStart()).isEqualTo(startDate);
        assertThat(booking.getEnd()).isEqualTo(endDate);
        assertThat(booking.getItem()).isEqualTo(item);
        assertThat(booking.getBooker()).isEqualTo(booker);
        assertThat(booking.getStatus()).isEqualTo(status);
    }
}
