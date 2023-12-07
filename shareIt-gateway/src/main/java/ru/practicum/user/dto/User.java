package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
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
