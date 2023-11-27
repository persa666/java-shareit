package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    Booking createBooking(@RequestBody @Valid BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    Booking replaceBooking(@RequestHeader("X-Sharer-User-Id") int userId, @PathParam("approved") boolean approved,
                           @PathVariable("bookingId") int bookingId) {
        return bookingService.replaceBooking(userId, approved, bookingId);
    }

    @GetMapping("/{bookingId}")
    Booking findBookingById(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable("bookingId") int bookingId) {
        return bookingService.findBookingById(userId, bookingId);
    }

    @GetMapping
    List<Booking> findBookingsByUserId(@RequestHeader("X-Sharer-User-Id") int userId, @RequestParam(required = false,
            defaultValue = "ALL") String state) {
        return bookingService.findBookingsByUserId(userId, state);
    }

    @GetMapping("/owner")
    List<Booking> findBookingForItemsByUserId(@RequestHeader("X-Sharer-User-Id") int userId,
                                              @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.findBookingForItemsByUserId(userId, state);
    }
}
