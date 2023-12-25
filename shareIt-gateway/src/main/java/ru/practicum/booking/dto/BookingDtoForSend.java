package ru.practicum.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.item.dto.ItemDtoForBooking;
import ru.practicum.user.dto.UserDtoForBooking;

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
