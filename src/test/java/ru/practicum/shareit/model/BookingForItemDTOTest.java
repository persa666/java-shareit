package ru.practicum.shareit.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingForItemDTO;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingForItemDTOTest {
    @Test
    public void testGetterSetter() {
        int testId = 1;
        int testBookerId = 2;

        BookingForItemDTO bookingDTO = new BookingForItemDTO();
        bookingDTO.setId(testId);
        bookingDTO.setBookerId(testBookerId);

        assertThat(bookingDTO.getId()).isEqualTo(testId);
        assertThat(bookingDTO.getBookerId()).isEqualTo(testBookerId);
    }

    @Test
    public void testAllArgsConstructor() {
        int testId = 1;
        int testBookerId = 2;

        BookingForItemDTO bookingDTO = new BookingForItemDTO(testId, testBookerId);

        assertThat(bookingDTO.getId()).isEqualTo(testId);
        assertThat(bookingDTO.getBookerId()).isEqualTo(testBookerId);
    }
}
