package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    @NotEmpty
    private String name;
    @Email
    @NotEmpty
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
