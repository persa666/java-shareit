package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForSend;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
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
                                                 @RequestParam(name = "from", defaultValue = "0")
                                                 @PositiveOrZero int from,
                                                 @RequestParam(name = "size", defaultValue = "30")
                                                 @Positive int size) {
        return bookingService.findBookingsByUserId(userId, state, PageRequest.of(from / size, size, Sort.by("start")));
    }

    @GetMapping("/owner")
    List<BookingDtoForSend> findBookingForItemsByUserId(@RequestHeader("X-Sharer-User-Id") int userId,
                                                        @RequestParam(required = false,
                                                                defaultValue = "ALL") String state,
                                                        @RequestParam(name = "from", defaultValue = "0")
                                                        @PositiveOrZero int from,
                                                        @RequestParam(name = "size", defaultValue = "30")
                                                        @Positive int size) {
        return bookingService.findBookingForItemsByUserId(userId, state, PageRequest.of(from / size, size, Sort.by("start")));
    }
}
