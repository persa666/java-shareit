package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForSend;
import ru.practicum.shareit.item.ItemDtoForBooking;
import ru.practicum.shareit.user.UserDtoForBooking;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingService bookingService;

    @Test
    void createBooking() throws Exception {
        int userId = 1;

        BookingDto bookingDto = new BookingDto(
                1,
                LocalDateTime.of(2024, 10, 10, 5, 10),
                LocalDateTime.of(2024, 10, 10, 10, 10)
        );

        BookingDtoForSend createdBooking = new BookingDtoForSend(
                1,
                LocalDateTime.of(2024, 10, 10, 5, 10),
                LocalDateTime.of(2024, 10, 10, 10, 10),
                Status.WAITING,
                new UserDtoForBooking(userId),
                new ItemDtoForBooking(1, "Вещь")
        );

        Mockito.when(bookingService.createBooking(bookingDto, userId)).thenReturn(createdBooking);

        mockMvc.perform(
                        post("/bookings")
                                .header("X-Sharer-User-Id", String.valueOf(userId))
                                .content(objectMapper.writeValueAsString(bookingDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(createdBooking)));

        Mockito.verify(bookingService, Mockito.times(1)).createBooking(bookingDto, userId);
        Mockito.verifyNoMoreInteractions(bookingService);
    }

    @Test
    void replaceBooking() throws Exception {
        int userId = 1;
        int bookingId = 1;
        boolean approved = true;

        BookingDtoForSend replacedBooking = new BookingDtoForSend(
                bookingId,
                LocalDateTime.of(2024, 10, 10, 5, 10),
                LocalDateTime.of(2024, 10, 10, 10, 10),
                Status.APPROVED,
                new UserDtoForBooking(userId),
                new ItemDtoForBooking(1, "Вещь")
        );

        Mockito.when(bookingService.replaceBooking(userId, approved, bookingId)).thenReturn(replacedBooking);

        mockMvc.perform(
                        patch("/bookings/{bookingId}", bookingId)
                                .header("X-Sharer-User-Id", String.valueOf(userId))
                                .param("approved", String.valueOf(approved))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(replacedBooking)));

        Mockito.verify(bookingService, Mockito.times(1)).replaceBooking(userId, approved, bookingId);
        Mockito.verifyNoMoreInteractions(bookingService);
    }

    @Test
    void findBookingById() throws Exception {
        int userId = 1;
        int bookingId = 1;

        BookingDtoForSend foundBooking = new BookingDtoForSend(
                bookingId,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                Status.WAITING,
                new UserDtoForBooking(userId),
                new ItemDtoForBooking(1, "Вещь")
        );

        Mockito.when(bookingService.findBookingById(userId, bookingId)).thenReturn(foundBooking);

        mockMvc.perform(
                        get("/bookings/{bookingId}", bookingId)
                                .header("X-Sharer-User-Id", String.valueOf(userId))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(foundBooking)));

        Mockito.verify(bookingService, Mockito.times(1)).findBookingById(userId, bookingId);
        Mockito.verifyNoMoreInteractions(bookingService);
    }

    @Test
    void findBookingsByUserId() throws Exception {
        int userId = 1;
        String state = "ALL";
        int from = 0;
        int size = 3;

        List<BookingDtoForSend> bookings = Arrays.asList(
                new BookingDtoForSend(
                        1,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(2),
                        Status.WAITING,
                        new UserDtoForBooking(userId),
                        new ItemDtoForBooking(1, "Вещь1")
                ),
                new BookingDtoForSend(
                        2,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(3),
                        Status.APPROVED,
                        new UserDtoForBooking(userId),
                        new ItemDtoForBooking(2, "Вещь2")
                ),
                new BookingDtoForSend(
                        3,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(4),
                        Status.REJECTED,
                        new UserDtoForBooking(userId),
                        new ItemDtoForBooking(3, "Вещь3")
                )
        );

        Mockito.when(bookingService.findBookingsByUserId(userId, state, from, size)).thenReturn(bookings);

        mockMvc.perform(
                        get("/bookings")
                                .header("X-Sharer-User-Id", String.valueOf(userId))
                                .param("state", state)
                                .param("from", String.valueOf(from))
                                .param("size", String.valueOf(size))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookings)));

        Mockito.verify(bookingService, Mockito.times(1)).findBookingsByUserId(userId, state, from, size);
        Mockito.verifyNoMoreInteractions(bookingService);
    }

    @Test
    void findBookingForItemsByUserId() throws Exception {
        int userId = 1;
        String state = "ALL";
        int from = 0;
        int size = 3;

        List<BookingDtoForSend> bookings = Arrays.asList(
                new BookingDtoForSend(
                        1,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(2),
                        Status.WAITING,
                        new UserDtoForBooking(userId),
                        new ItemDtoForBooking(1, "Вещь1")
                ),
                new BookingDtoForSend(
                        2,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(3),
                        Status.APPROVED,
                        new UserDtoForBooking(userId),
                        new ItemDtoForBooking(2, "Вещь2")
                ),
                new BookingDtoForSend(
                        3,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(4),
                        Status.REJECTED,
                        new UserDtoForBooking(userId),
                        new ItemDtoForBooking(3, "Вещь3")
                )
        );

        Mockito.when(bookingService.findBookingForItemsByUserId(userId, state, from, size)).thenReturn(bookings);

        mockMvc.perform(
                        get("/bookings/owner")
                                .header("X-Sharer-User-Id", String.valueOf(userId))
                                .param("state", state)
                                .param("from", String.valueOf(from))
                                .param("size", String.valueOf(size))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookings)));

        Mockito.verify(bookingService, Mockito.times(1)).findBookingForItemsByUserId(userId, state, from, size);
        Mockito.verifyNoMoreInteractions(bookingService);
    }
}
