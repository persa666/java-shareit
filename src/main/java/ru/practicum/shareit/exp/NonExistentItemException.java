package ru.practicum.shareit.exp;

public class NonExistentItemException extends RuntimeException {
    public NonExistentItemException(String message) {
        super(message);
    }
}
