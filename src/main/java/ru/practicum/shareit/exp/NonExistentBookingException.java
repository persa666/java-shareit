package ru.practicum.shareit.exp;

public class NonExistentBookingException extends RuntimeException {
    public NonExistentBookingException(String message) {
        super(message);
    }
}
