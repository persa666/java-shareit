package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForSend;

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
    BookingDtoForSend createBooking(@RequestBody @Valid BookingDto bookingDto,
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
                                                 @RequestParam(required = false, defaultValue = "ALL") String state,
                                                 @RequestParam(name = "from", defaultValue = "0") int from,
                                                 @RequestParam(name = "size", defaultValue = "30") int size) {
        return bookingService.findBookingsByUserId(userId, state, from, size);
    }

    @GetMapping("/owner")
    List<BookingDtoForSend> findBookingForItemsByUserId(@RequestHeader("X-Sharer-User-Id") int userId,
                                                        @RequestParam(required = false,
                                                                defaultValue = "ALL") String state,
                                                        @RequestParam(name = "from", defaultValue = "0") int from,
                                                        @RequestParam(name = "size", defaultValue = "30") int size) {
        return bookingService.findBookingForItemsByUserId(userId, state, from, size);
    }
}
