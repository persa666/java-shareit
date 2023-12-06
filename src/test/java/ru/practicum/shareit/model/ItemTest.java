package ru.practicum.shareit.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemTest {
    @Test
    public void testAllArgsConstructor() {
        int testId = 1;
        User testOwner = new User("Test Owner", "owner@example.com");
        String testName = "Test Item";
        String testDescription = "This is a test item.";
        boolean testAvailable = true;
        ItemRequest testRequest = new ItemRequest();

        Item item = new Item(testId, testOwner, testName, testDescription, testAvailable, testRequest);

        assertThat(item.getId()).isEqualTo(testId);
        assertThat(item.getOwner()).isEqualTo(testOwner);
        assertThat(item.getName()).isEqualTo(testName);
        assertThat(item.getDescription()).isEqualTo(testDescription);
        assertThat(item.getAvailable()).isEqualTo(testAvailable);
        assertThat(item.getRequest()).isEqualTo(testRequest);
    }

    @Test
    public void testGetRequestIdOrNullWithRequest() {
        ItemRequest testRequest = new ItemRequest();
        testRequest.setId(1);
        Item item = new Item();
        item.setRequest(testRequest);

        Integer requestId = item.getRequestIdOrNull();

        assertThat(requestId).isEqualTo(1);
    }

    @Test
    public void testGetRequestIdOrNullWithoutRequest() {
        Item item = new Item();
        Integer requestId = item.getRequestIdOrNull();

        assertThat(requestId).isEqualTo(null);
    }

    @Test
    public void testConstructor() {
        User testOwner = new User("Test Owner", "owner@example.com");
        String testName = "Test Item";
        String testDescription = "This is a test item.";
        boolean testAvailable = true;
        ItemRequest testRequest = new ItemRequest();

        Item item = new Item(testOwner, testName, testDescription, testAvailable, testRequest);

        assertThat(item.getOwner()).isEqualTo(testOwner);
        assertThat(item.getName()).isEqualTo(testName);
        assertThat(item.getDescription()).isEqualTo(testDescription);
        assertThat(item.getAvailable()).isEqualTo(testAvailable);
        assertThat(item.getRequest()).isEqualTo(testRequest);
    }
}
