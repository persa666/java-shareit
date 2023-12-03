package ru.practicum.shareit.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingForItemDTO;
import ru.practicum.shareit.item.CommentDtoForSend;
import ru.practicum.shareit.item.ItemBookingDto;
import ru.practicum.shareit.request.ItemRequest;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemBookingDtoTest {
    @Test
    public void testDataAnnotation() {
        int testId = 1;
        String testName = "Test Item";
        String testDescription = "Test Item Description";
        Boolean testAvailable = true;
        ItemRequest testRequest = new ItemRequest();
        BookingForItemDTO testLastBooking = new BookingForItemDTO();
        BookingForItemDTO testNextBooking = new BookingForItemDTO();
        List<CommentDtoForSend> testComments = Collections.singletonList(new CommentDtoForSend());

        ItemBookingDto itemBookingDto = new ItemBookingDto();
        itemBookingDto.setId(testId);
        itemBookingDto.setName(testName);
        itemBookingDto.setDescription(testDescription);
        itemBookingDto.setAvailable(testAvailable);
        itemBookingDto.setRequest(testRequest);
        itemBookingDto.setLastBooking(testLastBooking);
        itemBookingDto.setNextBooking(testNextBooking);
        itemBookingDto.setComments(testComments);

        assertThat(itemBookingDto.getId()).isEqualTo(testId);
        assertThat(itemBookingDto.getName()).isEqualTo(testName);
        assertThat(itemBookingDto.getDescription()).isEqualTo(testDescription);
        assertThat(itemBookingDto.getAvailable()).isEqualTo(testAvailable);
        assertThat(itemBookingDto.getRequest()).isEqualTo(testRequest);
        assertThat(itemBookingDto.getLastBooking()).isEqualTo(testLastBooking);
        assertThat(itemBookingDto.getNextBooking()).isEqualTo(testNextBooking);
        assertThat(itemBookingDto.getComments()).isEqualTo(testComments);
    }
}
