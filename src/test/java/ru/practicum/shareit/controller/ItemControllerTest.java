package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemService itemService;

    @Test
    void createItem() throws Exception {
        int userId = 1;

        ItemDtoForRequest item = new ItemDtoForRequest(
                0,
                "Дрель",
                "Простая дрель",
                true,
                null
        );

        ItemDtoForRequest savedItem = new ItemDtoForRequest(
                userId,
                "Дрель",
                "Простая дрель",
                true,
                null
        );

        Mockito.when(itemService.createItem(item, userId)).thenReturn(savedItem);

        mockMvc.perform(
                        post("/items")
                                .header("X-Sharer-User-Id", String.valueOf(userId))
                                .content(objectMapper.writeValueAsString(item))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(savedItem)));

        Mockito.verify(itemService, Mockito.times(1)).createItem(item, userId);
        Mockito.verifyNoMoreInteractions(itemService);
    }

    @Test
    void replaceItem() throws Exception {
        int userId = 1;
        int itemId = 1;

        ItemDto item = new ItemDto(
                itemId,
                "Дрель+",
                "Аккумуляторная дрель",
                false
        );

        ItemDto itemDto = new ItemDto(
                itemId,
                "Дрель+",
                "Аккумуляторная дрель",
                false
        );

        Mockito.when(itemService.replaceItem(itemDto, userId, itemId)).thenReturn(item);

        mockMvc.perform(
                        patch("/items/{itemId}", itemId)
                                .header("X-Sharer-User-Id", String.valueOf(userId))
                                .content(objectMapper.writeValueAsString(item))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(item)));

        Mockito.verify(itemService, Mockito.times(1)).replaceItem(itemDto, userId, itemId);
        Mockito.verifyNoMoreInteractions(itemService);
    }

    @Test
    void getItemById() throws Exception {
        int userId = 1;
        int itemId = 1;

        ItemBookingDto item = new ItemBookingDto(
                itemId,
                "Дрель+",
                "Аккумуляторная дрель",
                true,
                null,
                null,
                null,
                new ArrayList<>()
        );

        Mockito.when(itemService.getItemById(userId, itemId)).thenReturn(item);

        mockMvc.perform(
                        get("/items/{itemId}", itemId)
                                .header("X-Sharer-User-Id", String.valueOf(userId))
                                .content(objectMapper.writeValueAsString(item))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(item)));

        Mockito.verify(itemService, Mockito.times(1)).getItemById(userId, itemId);
        Mockito.verifyNoMoreInteractions(itemService);
    }

    @Test
    void getItemsOwnerById() throws Exception {
        int userId = 1;
        int from = 3;
        int size = 1;

        List<ItemBookingDto> items = Arrays.asList(
                new ItemBookingDto(
                        1,
                        "Item1",
                        "Description1",
                        true,
                        null,
                        null,
                        null,
                        new ArrayList<>()),
                new ItemBookingDto(
                        2,
                        "Item2",
                        "Description2",
                        false,
                        null,
                        null,
                        null,
                        new ArrayList<>()),
                new ItemBookingDto(
                        3,
                        "Item3",
                        "Description3",
                        true,
                        null,
                        null,
                        null,
                        new ArrayList<>())
        );

        Mockito.when(itemService.getItemsOwnerById(userId, from, size)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", String.valueOf(userId))
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new ArrayList<>())));

        Mockito.verify(itemService, Mockito.times(1)).getItemsOwnerById(userId, from, size);
        Mockito.verifyNoMoreInteractions(itemService);
    }

    @Test
    void getItemBySearch() throws Exception {
        int userId = 1;
        String searchText = "Name";
        int from = 0;
        int size = 3;

        List<ItemDto> items = Arrays.asList(
                new ItemDto(1, "Name1", "Description1", true),
                new ItemDto(2, "Name2", "Description2", false),
                new ItemDto(3, "Name3", "Description3", true)
        );

        Mockito.when(itemService.getItemBySearch(userId, searchText, from, size)).thenReturn(items);

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", String.valueOf(userId))
                        .param("text", searchText)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(items)));

        Mockito.verify(itemService, Mockito.times(1)).getItemBySearch(userId, searchText, from, size);
        Mockito.verifyNoMoreInteractions(itemService);

    }

    @Test
    void createComment() throws Exception {
        int userId = 1;
        int itemId = 1;

        CommentDto commentDto = new CommentDto("Хорошая вещь");

        CommentDtoForSend createdComment = new CommentDtoForSend(
                1,
                "Хорошая вещь",
                "Имя",
                LocalDateTime.now()
        );

        Mockito.when(itemService.createComment(userId, commentDto, itemId)).thenReturn(createdComment);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", String.valueOf(userId))
                        .content(objectMapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(createdComment)));

        Mockito.verify(itemService, Mockito.times(1)).createComment(userId, commentDto, itemId);
        Mockito.verifyNoMoreInteractions(itemService);
    }
}
