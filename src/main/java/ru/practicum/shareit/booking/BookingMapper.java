package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

public class BookingMapper {
    public static Booking toBooking(BookingDto bookingDto) {
        return new Booking(
                0,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                new Item(),
                new User(),
                Status.WAITING
        );
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getItem().getId(),
                booking.getStart(),
                booking.getEnd()
        );
    }
}
