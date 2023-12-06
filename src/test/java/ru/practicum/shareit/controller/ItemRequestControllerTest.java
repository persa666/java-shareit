package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemDtoForRequest;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForSend;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItem;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
public class ItemRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemRequestService itemRequestService;

    @Test
    void createRequest() throws Exception {
        int userId = 1;
        int requestId = 1;
        ItemRequestDto itemRequestDto = new ItemRequestDto(
                "Дрель аккумуляторная"
        );

        ItemRequestDtoForSend createdRequest = new ItemRequestDtoForSend(
                requestId,
                "Дрель аккумуляторная",
                LocalDateTime.now()
        );

        Mockito.when(itemRequestService.createRequest(userId, itemRequestDto)).thenReturn(createdRequest);

        mockMvc.perform(
                        post("/requests")
                                .header("X-Sharer-User-Id", String.valueOf(userId))
                                .content(objectMapper.writeValueAsString(itemRequestDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(createdRequest)));

        Mockito.verify(itemRequestService, Mockito.times(1)).createRequest(userId, itemRequestDto);
        Mockito.verifyNoMoreInteractions(itemRequestService);

    }

    @Test
    void getAllRequestsUser() throws Exception {
        int userId = 1;

        List<ItemRequestDtoWithItem> requests = Arrays.asList(
                new ItemRequestDtoWithItem(1, "Запрос 1", LocalDateTime.now(), new ArrayList<>()),
                new ItemRequestDtoWithItem(2, "Запрос 2", LocalDateTime.now(), new ArrayList<>())
        );

        Mockito.when(itemRequestService.getAllRequestsUser(userId)).thenReturn(requests);

        mockMvc.perform(
                        get("/requests")
                                .header("X-Sharer-User-Id", String.valueOf(userId))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(requests)));

        Mockito.verify(itemRequestService, Mockito.times(1)).getAllRequestsUser(userId);
        Mockito.verifyNoMoreInteractions(itemRequestService);
    }

    @Test
    void getAllRequestOtherUsers() throws Exception {
        int userId = 1;
        int from = 0;
        int size = 3;

        List<ItemRequestDtoWithItem> requests = Arrays.asList(
                new ItemRequestDtoWithItem(1, "Запрос 1", LocalDateTime.now(), new ArrayList<>()),
                new ItemRequestDtoWithItem(2, "Запрос 2", LocalDateTime.now(), new ArrayList<>()),
                new ItemRequestDtoWithItem(3, "Запрос 3", LocalDateTime.now(), new ArrayList<>())
        );

        Mockito.when(itemRequestService.getAllRequestOtherUsers(userId, PageRequest.of(from / size, size,
                Sort.by("created")))).thenReturn(requests);

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", String.valueOf(userId))
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(requests)));

        Mockito.verify(itemRequestService, Mockito.times(1))
                .getAllRequestOtherUsers(userId, PageRequest.of(from / size, size, Sort.by("created")));
        Mockito.verifyNoMoreInteractions(itemRequestService);
    }

    @Test
    void getRequestById() throws Exception {
        int userId = 1;
        int requestId = 1;

        ItemRequestDtoWithItem request = new ItemRequestDtoWithItem(
                requestId,
                "Request",
                LocalDateTime.now(),
                Arrays.asList(
                        new ItemDtoForRequest(
                                1,
                                "name",
                                "description",
                                true,
                                null
                        ),
                        new ItemDtoForRequest(
                                1,
                                "name",
                                "description",
                                false,
                                3
                        ))
        );

        Mockito.when(itemRequestService.getRequestById(userId, requestId)).thenReturn(request);

        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", String.valueOf(userId))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(request)));

        Mockito.verify(itemRequestService, Mockito.times(1)).getRequestById(userId, requestId);
        Mockito.verifyNoMoreInteractions(itemRequestService);
    }
}
