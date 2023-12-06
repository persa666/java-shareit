package ru.practicum.shareit.exp;

public class NonExistentItemRequestException extends RuntimeException {
    public NonExistentItemRequestException(String message) {
        super(message);
    }
}
