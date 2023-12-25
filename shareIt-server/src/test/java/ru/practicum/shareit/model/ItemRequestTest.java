package ru.practicum.shareit.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemRequestTest {
    @Test
    public void testDataAnnotation() {
        int testId = 1;
        String testDescription = "Test Description";
        User testRequestor = new User("Test User", "test.user@example.com");
        LocalDateTime testCreated = LocalDateTime.now();

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(testId);
        itemRequest.setDescription(testDescription);
        itemRequest.setRequestor(testRequestor);
        itemRequest.setCreated(testCreated);

        assertThat(itemRequest.getId()).isEqualTo(testId);
        assertThat(itemRequest.getDescription()).isEqualTo(testDescription);
        assertThat(itemRequest.getRequestor()).isEqualTo(testRequestor);
        assertThat(itemRequest.getCreated()).isEqualTo(testCreated);
    }

    @Test
    public void testAllArgsConstructor() {
        int testId = 1;
        String testDescription = "Test Description";
        User testRequestor = new User("Test User", "test.user@example.com");
        LocalDateTime testCreated = LocalDateTime.now();

        ItemRequest itemRequest = new ItemRequest(testId, testDescription, testRequestor, testCreated);

        assertThat(itemRequest.getId()).isEqualTo(testId);
        assertThat(itemRequest.getDescription()).isEqualTo(testDescription);
        assertThat(itemRequest.getRequestor()).isEqualTo(testRequestor);
        assertThat(itemRequest.getCreated()).isEqualTo(testCreated);
    }
}
