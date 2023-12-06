package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @Test
    void createUserSuccess() throws Exception {
        UserDto userDto = new UserDto(
                0,
                "user",
                "user@user.com"
        );

        UserDto savedUserDto = new UserDto();
        savedUserDto.setId(1);
        savedUserDto.setName("user");
        savedUserDto.setEmail("user@user.com");

        Mockito.when(userService.createUser(userDto)).thenReturn(savedUserDto);
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(userDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(savedUserDto)));

        Mockito.verify(userService, Mockito.times(1)).createUser(userDto);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void getAllUsers() throws Exception {
        List<UserDto> userList = Arrays.asList(
                new UserDto(1, "User1", "user1@example.com"),
                new UserDto(2, "User2", "user2@example.com")
        );

        Mockito.when(userService.getAllUsers()).thenReturn(userList);

        mockMvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userList)));

        Mockito.verify(userService, Mockito.times(1)).getAllUsers();
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void getUserById() throws Exception {
        int userId = 1;
        UserDto userDto = new UserDto(
                userId,
                "user",
                "user@user.com"
        );

        Mockito.when(userService.getUserById(userId)).thenReturn(userDto);

        mockMvc.perform(get("/users/{userId}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));

        Mockito.verify(userService, Mockito.times(1)).getUserById(userId);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void replaceUser() throws Exception {
        int userId = 1;
        UserDto userDto = new UserDto(
                userId,
                "user",
                "user@user.com"
        );

        Mockito.when(userService.replaceUser(userDto, userId)).thenReturn(userDto);

        mockMvc.perform(patch("/users/{userId}", userId)
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));

        Mockito.verify(userService, Mockito.times(1)).replaceUser(userDto, userId);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void deleteUser() throws Exception {
        int userId = 1;


        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).deleteUser(userId);
        Mockito.verifyNoMoreInteractions(userService);
    }
}