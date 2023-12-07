package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingState;
import ru.practicum.exp.StatusException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingControllerGateway {
    private final BookingClient bookingClient;

    @PostMapping
    ResponseEntity<Object> createBooking(@RequestBody @Valid BookingDto bookingDto,
                                         @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingClient.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    ResponseEntity<Object> replaceBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                          @RequestParam("approved") Boolean approved,
                                          @PathVariable("bookingId") int bookingId) {
        return bookingClient.replaceBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    ResponseEntity<Object> findBookingById(@RequestHeader("X-Sharer-User-Id") int userId,
                                           @PathVariable("bookingId") int bookingId) {
        return bookingClient.findBookingById(userId, bookingId);
    }

    @GetMapping
    ResponseEntity<Object> findBookingsByUserId(@RequestHeader("X-Sharer-User-Id") int userId,
                                                @RequestParam(required = false, defaultValue = "ALL", name = "state")
                                                String stateParam,
                                                @RequestParam(name = "from", defaultValue = "0")
                                                @PositiveOrZero int from,
                                                @RequestParam(name = "size", defaultValue = "30")
                                                @Positive int size) {
        try {
            BookingState state = BookingState.valueOf(stateParam.toUpperCase());
            return bookingClient.findBookingsByUserId(userId, state, from, size);
        } catch (RuntimeException e) {
            throw new StatusException("Unknown state: " + stateParam);
        }
    }

    @GetMapping("/owner")
    ResponseEntity<Object> findBookingForItemsByUserId(@RequestHeader("X-Sharer-User-Id") int userId,
                                                       @RequestParam(required = false,
                                                               defaultValue = "ALL", name = "state") String stateParam,
                                                       @RequestParam(name = "from", defaultValue = "0")
                                                       @PositiveOrZero int from,
                                                       @RequestParam(name = "size", defaultValue = "30")
                                                       @Positive int size) {
        try {
            BookingState state = BookingState.valueOf(stateParam.toUpperCase());
            return bookingClient.findBookingForItemsByUserId(userId, state, from, size);
        } catch (RuntimeException e) {
            throw new StatusException("Unknown state: " + stateParam);
        }
    }
}
