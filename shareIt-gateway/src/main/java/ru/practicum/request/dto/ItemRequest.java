package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.user.dto.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    private int id;

    private String description;

    private User requestor;

    private LocalDateTime created;

    public ItemRequest(String description, User requestor, LocalDateTime created) {
        this.description = description;
        this.requestor = requestor;
        this.created = created;
    }
}
