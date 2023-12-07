package ru.practicum.shareit.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestDtoForSend;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemRequestDtoForSendTest {
    @Test
    public void testDataAnnotation() {
        int testId = 1;
        String testDescription = "Test Description";
        LocalDateTime testCreated = LocalDateTime.now();

        ItemRequestDtoForSend itemRequestDto = new ItemRequestDtoForSend();
        itemRequestDto.setId(testId);
        itemRequestDto.setDescription(testDescription);
        itemRequestDto.setCreated(testCreated);

        assertThat(itemRequestDto.getId()).isEqualTo(testId);
        assertThat(itemRequestDto.getDescription()).isEqualTo(testDescription);
        assertThat(itemRequestDto.getCreated()).isEqualTo(testCreated);
    }



}
