package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.ItemDtoForBooking;
import ru.practicum.shareit.user.UserDtoForBooking;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoForSend {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private UserDtoForBooking booker;
    private ItemDtoForBooking item;
}
