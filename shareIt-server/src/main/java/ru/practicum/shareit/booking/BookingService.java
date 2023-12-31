package ru.practicum.shareit.booking;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForSend;

import java.util.List;

public interface BookingService {
    BookingDtoForSend createBooking(BookingDto bookingDto, int userId);

    BookingDtoForSend replaceBooking(int userId, boolean approved, int bookingId);

    BookingDtoForSend findBookingById(int userId, int bookingId);

    List<BookingDtoForSend> findBookingsByUserId(int userId, String state, PageRequest pageRequest);

    List<BookingDtoForSend> findBookingForItemsByUserId(int userId, String state, PageRequest pageRequest);
}
