package ru.practicum.shareit.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.ItemDtoForBooking;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemDtoForBookingTest {
    @Test
    public void testDataAnnotation() {
        int testId = 1;
        String testName = "Test Item";

        ItemDtoForBooking itemDtoForBooking = new ItemDtoForBooking();
        itemDtoForBooking.setId(testId);
        itemDtoForBooking.setName(testName);

        assertThat(itemDtoForBooking.getId()).isEqualTo(testId);
        assertThat(itemDtoForBooking.getName()).isEqualTo(testName);
    }
}
