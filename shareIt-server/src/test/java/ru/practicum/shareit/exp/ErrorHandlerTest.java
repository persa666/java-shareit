package ru.practicum.shareit.exp;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exp.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ErrorHandlerTest {
    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void handleUserEmailExistsTest() {
        EmailExistsException exception = new EmailExistsException("User with this email already exists");
        ErrorResponse errorResponse = errorHandler.handleUserEmailExists(exception);

        assertNotNull(errorResponse);
        assertEquals("Ошибка с созданием пользователя", errorResponse.getError());
        assertEquals("User with this email already exists", errorResponse.getDescription());
    }

    @Test
    void handleUserNotFoundTest() {
        NonExistentUserException exception = new NonExistentUserException("User not found");
        ErrorResponse errorResponse = errorHandler.handleUserNotFound(exception);

        assertNotNull(errorResponse);
        assertEquals("Ошибка с поиском пользователя.", errorResponse.getError());
        assertEquals("User not found", errorResponse.getDescription());
    }

    @Test
    void handleItemNotFoundTest() {
        NonExistentItemException exception = new NonExistentItemException("Item not found");
        ErrorResponse errorResponse = errorHandler.handleItemNotFound(exception);

        assertNotNull(errorResponse);
        assertEquals("Ошибка с поиском вещи.", errorResponse.getError());
        assertEquals("Item not found", errorResponse.getDescription());
    }

    @Test
    void handleBookingNotFoundTest() {
        NonExistentBookingException exception = new NonExistentBookingException("Booking not found");
        ErrorResponse errorResponse = errorHandler.handleBookingNotFound(exception);

        assertNotNull(errorResponse);
        assertEquals("Ошибка с поиском бронирования.", errorResponse.getError());
        assertEquals("Booking not found", errorResponse.getDescription());
    }

    @Test
    void handleRequestNotFoundTest() {
        NonExistentItemRequestException exception = new NonExistentItemRequestException("Request not found");
        ErrorResponse errorResponse = errorHandler.handleRequestNotFound(exception);

        assertNotNull(errorResponse);
        assertEquals("Ошибка с поиском запроса.", errorResponse.getError());
        assertEquals("Request not found", errorResponse.getDescription());
    }

    @Test
    void handleBookingBadRequestTest() {
        BookingException exception = new BookingException("Booking bad request");
        ErrorResponse errorResponse = errorHandler.handleBookingBadRequest(exception);

        assertNotNull(errorResponse);
        assertEquals("Ошибка с бронированием вещи.", errorResponse.getError());
        assertEquals("Booking bad request", errorResponse.getDescription());
    }

    @Test
    void handleStatusBadRequestTest() {
        StatusException exception = new StatusException("Status bad request");
        ErrorResponse errorResponse = errorHandler.handleStatusBadRequest(exception);

        assertNotNull(errorResponse);
        assertEquals("Unknown state: UNSUPPORTED_STATUS", errorResponse.getError());
        assertEquals("Status bad request", errorResponse.getDescription());
    }

    @Test
    void handleCommentBadRequestTest() {
        CommentException exception = new CommentException("Comment bad request");
        ErrorResponse errorResponse = errorHandler.handleCommentBadRequest(exception);

        assertNotNull(errorResponse);
        assertEquals("Ошибка с добавлением статуса.", errorResponse.getError());
        assertEquals("Comment bad request", errorResponse.getDescription());
    }

    @Test
    void handleCountsBadRequestTest() {
        CountsException exception = new CountsException("Counts bad request");
        ErrorResponse errorResponse = errorHandler.handleCountsBadRequest(exception);

        assertNotNull(errorResponse);
        assertEquals("Ошибка с параметрами для пагинациии.", errorResponse.getError());
        assertEquals("Counts bad request", errorResponse.getDescription());
    }
}
