package ru.practicum.shareit.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.ItemDtoForRequest;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemRequestDtoWithItemTest {
    @Test
    public void testDataAnnotation() {
        int testId = 1;
        String testDescription = "Test Description";
        LocalDateTime testCreated = LocalDateTime.now();
        List<ItemDtoForRequest> testItems = new ArrayList<>();

        ItemRequestDtoWithItem itemRequestDto = new ItemRequestDtoWithItem();
        itemRequestDto.setId(testId);
        itemRequestDto.setDescription(testDescription);
        itemRequestDto.setCreated(testCreated);
        itemRequestDto.setItems(testItems);

        assertThat(itemRequestDto.getId()).isEqualTo(testId);
        assertThat(itemRequestDto.getDescription()).isEqualTo(testDescription);
        assertThat(itemRequestDto.getCreated()).isEqualTo(testCreated);
        assertThat(itemRequestDto.getItems()).isEqualTo(testItems);
    }
}
