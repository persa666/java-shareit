package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForSend;

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
    BookingDtoForSend createBooking(@RequestBody BookingDto bookingDto,
                                    @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    BookingDtoForSend replaceBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                     @PathParam("approved") boolean approved,
                                     @PathVariable("bookingId") int bookingId) {
        return bookingService.replaceBooking(userId, approved, bookingId);
    }

    @GetMapping("/{bookingId}")
    BookingDtoForSend findBookingById(@RequestHeader("X-Sharer-User-Id") int userId,
                                      @PathVariable("bookingId") int bookingId) {
        return bookingService.findBookingById(userId, bookingId);
    }

    @GetMapping
    List<BookingDtoForSend> findBookingsByUserId(@RequestHeader("X-Sharer-User-Id") int userId,
                                                 @RequestParam String state,
                                                 @RequestParam int from,
                                                 @RequestParam int size) {
        return bookingService.findBookingsByUserId(userId, state, PageRequest.of(from / size, size));
    }

    @GetMapping("/owner")
    List<BookingDtoForSend> findBookingForItemsByUserId(@RequestHeader("X-Sharer-User-Id") int userId,
                                                        @RequestParam String state,
                                                        @RequestParam int from,
                                                        @RequestParam int size) {
        return bookingService.findBookingForItemsByUserId(userId, state, PageRequest.of(from / size, size));
    }
}
