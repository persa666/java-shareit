package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingForItemDTO;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemBookingDto {
    private int id;
    private User owner;
    private String name;
    private String description;
    private Boolean available;
    private ItemRequest request;
    private BookingForItemDTO lastBooking;
    private BookingForItemDTO nextBooking;
    private List<CommentDtoForSend> comments;
}
