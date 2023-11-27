package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    Booking createBooking(BookingDto bookingDto, int userId);

    Booking replaceBooking(int userId, boolean approved, int bookingId);

    Booking findBookingById(int userId, int bookingId);

    List<Booking> findBookingsByUserId(int userId, String state);

    List<Booking> findBookingForItemsByUserId(int userId, String state);
}
