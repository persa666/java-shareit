package ru.practicum.shareit.exp;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUserEmailExists(final EmailExistsException e) {
        return new ErrorResponse(
                "Ошибка с созданием пользователя",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(final NonExistentUserException e) {
        return new ErrorResponse(
                "Ошибка с поиском пользователя.", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleItemNotFound(final NonExistentItemException e) {
        return new ErrorResponse(
                "Ошибка с поиском вещи.", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBookingNotFound(final NonExistentBookingException e) {
        return new ErrorResponse(
                "Ошибка с поиском бронирования.", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBookingBadRequest(final BookingException e) {
        return new ErrorResponse(
                "Ошибка с бронированием вещи.", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleStatusBadRequest(final StatusException e) {
        return new ErrorResponse(
                "Unknown state: UNSUPPORTED_STATUS", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCommentBadRequest(final CommentException e) {
        return new ErrorResponse(
                "Ошибка с добавлением статуса.", e.getMessage()
        );
    }
}
