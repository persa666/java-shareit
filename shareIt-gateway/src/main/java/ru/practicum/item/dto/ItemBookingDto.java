package ru.practicum.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.booking.dto.BookingForItemDTO;
import ru.practicum.request.dto.ItemRequest;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemBookingDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private ItemRequest request;
    private BookingForItemDTO lastBooking;
    private BookingForItemDTO nextBooking;
    private List<CommentDtoForSend> comments;
}
