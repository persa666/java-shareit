package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exp.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Booking createBooking(BookingDto bookingDto, int userId) {
        try {
            if (!bookingDto.getStart().isBefore(bookingDto.getEnd())) {
                throw new BookingException("Неверные даты бронирования вещи.");
            }
            Item item = itemRepository.getById(bookingDto.getItemId());
            if (item.getOwner().getId() == userId) {
                throw new NonExistentUserException("Пользователь является владельцем вещи.");
            }
            if (!item.getAvailable()) {
                throw new BookingException("Вещь недоступна для бронирования.");
            }
            if (userRepository.countUserById(userId) <= 0) {
                throw new NonExistentUserException("Пользователя с таким id нет.");
            }
            if (bookingRepository.saveByItemId(bookingDto.getItemId(), bookingDto.getStart(),
                    bookingDto.getEnd(), userId, Status.WAITING.toString()) <= 0) {
                throw new NonExistentItemException("Неверные параметры для бронирования.");
            }
            return bookingRepository.findByItemIdAndStartAndEnd(bookingDto.getItemId(), bookingDto.getStart(),
                    bookingDto.getEnd());
        } catch (EntityNotFoundException exception) {
            throw new NonExistentItemException("Вещи с таким id нет.");
        }
    }

    @Override
    public Booking replaceBooking(int userId, boolean approved, int bookingId) {
        Booking booking = bookingRepository.findByIdAndStatus(bookingId, Status.APPROVED);
        if (booking != null) {
            throw new BookingException("Бронирование уже подтверждено.");
        }
        if (approved) {
            if (bookingRepository.updateStatusByBookingId(userId, Status.APPROVED, bookingId) <= 0) {
                throw new NonExistentUserException("Пользователь не является владельцем вещи");
            }
        } else {
            if (bookingRepository.updateStatusByBookingId(userId, Status.REJECTED, bookingId) <= 0) {
                throw new BookingException("Пользователь не является владельцем вещи");
            }
        }
        return bookingRepository.findById(bookingId);
    }

    @Override
    public Booking findBookingById(int userId, int bookingId) {
        if (userRepository.countUserById(userId) <= 0) {
            throw new NonExistentUserException("Пользователя с таким id нет.");
        }
        Booking booking = bookingRepository.findBookingByIdByOwnerId(userId, bookingId);
        if (booking == null) {
            throw new NonExistentBookingException("Бронирования с таким id нет.");
        }
        return booking;
    }

    @Override
    public List<Booking> findBookingsByUserId(int userId, String state) {
        if (userRepository.countUserById(userId) <= 0) {
            throw new NonExistentUserException("Пользователя с таким id нет.");
        }
        switch (state) {
            case "ALL": {
                return new ArrayList<>(bookingRepository.findByBookerIdOrderByStartDesc(userId));
            }
            case "CURRENT": {
                return bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now());
            }
            case "PAST": {
                return bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
            }
            case "FUTURE": {
                return bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
            }
            case "WAITING":
                return bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
            case "REJECTED": {
                return bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
            }
            default: {
                throw new StatusException("Статус бронирования неверный");
            }

        }
    }

    @Override
    public List<Booking> findBookingForItemsByUserId(int userId, String state) {
        if (userRepository.countUserById(userId) <= 0) {
            throw new NonExistentUserException("Пользователя с таким id нет.");
        }
        switch (state) {
            case "ALL": {
                return bookingRepository.findByItemOwnerIdOrderByStartDesc(userId);
            }
            case "CURRENT": {
                return bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now());
            }
            case "PAST": {
                return bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
            }
            case "FUTURE": {
                return bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
            }
            case "WAITING":
                return bookingRepository.findByItemOwnerIdAndStatus(userId, Status.WAITING);
            case "REJECTED": {
                return bookingRepository.findByItemOwnerIdAndStatus(userId, Status.REJECTED);
            }
            default: {
                throw new StatusException("Статус бронирования неверный");
            }
        }
    }
}
