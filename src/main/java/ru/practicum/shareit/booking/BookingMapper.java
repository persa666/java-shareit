package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForSend;
import ru.practicum.shareit.item.ItemDtoForBooking;
import ru.practicum.shareit.user.UserDtoForBooking;

public class BookingMapper {
    public static BookingDtoForSend toBookingForSend(Booking booking) {
        return new BookingDtoForSend(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                new UserDtoForBooking(booking.getBooker().getId()),
                new ItemDtoForBooking(booking.getItem().getId(), booking.getItem().getName())
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
