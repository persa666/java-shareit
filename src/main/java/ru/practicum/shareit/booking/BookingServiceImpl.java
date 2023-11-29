package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForSend;
import ru.practicum.shareit.exp.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDtoForSend createBooking(BookingDto bookingDto, int userId) {
        try {
            if (!bookingDto.getStart().isBefore(bookingDto.getEnd())) {
                throw new BookingException("Неверные даты бронирования вещи.");
            }
            Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                    new NonExistentBookingException("Бронирование с таким id не найден."));
            if (item.getOwner().getId() == userId) {
                throw new NonExistentUserException("Пользователь является владельцем вещи.");
            }
            if (!item.getAvailable()) {
                throw new BookingException("Вещь недоступна для бронирования.");
            }
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NonExistentUserException("Пользователя с таким id нет."));
            bookingRepository.save(new Booking(0, bookingDto.getStart(), bookingDto.getEnd(), item,
                    user, Status.WAITING));
            return BookingMapper.toBookingForSend(bookingRepository
                    .findByItemIdAndStartAndEnd(bookingDto.getItemId(), bookingDto.getStart(), bookingDto.getEnd()));
        } catch (EntityNotFoundException exception) {
            throw new NonExistentItemException("Вещи с таким id нет.");
        }
    }

    @Override
    public BookingDtoForSend replaceBooking(int userId, boolean approved, int bookingId) {
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
        return BookingMapper.toBookingForSend(bookingRepository.findById(bookingId).orElseThrow(() ->
                new NonExistentBookingException("Бронирование с таким id не найден.")));
    }

    @Override
    public BookingDtoForSend findBookingById(int userId, int bookingId) {
        if (userRepository.countUserById(userId) <= 0) {
            throw new NonExistentUserException("Пользователя с таким id нет.");
        }
        Booking booking = bookingRepository.findBookingByIdByOwnerId(userId, bookingId);
        if (booking == null) {
            throw new NonExistentBookingException("Бронирования с таким id нет.");
        }
        return BookingMapper.toBookingForSend(booking);
    }

    @Override
    public List<BookingDtoForSend> findBookingsByUserId(int userId, String state) {
        if (userRepository.countUserById(userId) <= 0) {
            throw new NonExistentUserException("Пользователя с таким id нет.");
        }
        switch (state) {
            case "ALL": {
                return bookingRepository.findByBookerIdOrderByStartDesc(userId)
                        .stream()
                        .map(BookingMapper::toBookingForSend)
                        .collect(Collectors.toList());
            }
            case "CURRENT": {
                return bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                                LocalDateTime.now(), LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingForSend)
                        .collect(Collectors.toList());
            }
            case "PAST": {
                return bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingForSend)
                        .collect(Collectors.toList());
            }
            case "FUTURE": {
                return bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingForSend)
                        .collect(Collectors.toList());
            }
            case "WAITING":
                return bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING)
                        .stream()
                        .map(BookingMapper::toBookingForSend)
                        .collect(Collectors.toList());
            case "REJECTED": {
                return bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED)
                        .stream()
                        .map(BookingMapper::toBookingForSend)
                        .collect(Collectors.toList());
            }
            default: {
                throw new StatusException("Статус бронирования неверный");
            }

        }
    }

    @Override
    public List<BookingDtoForSend> findBookingForItemsByUserId(int userId, String state) {
        if (userRepository.countUserById(userId) <= 0) {
            throw new NonExistentUserException("Пользователя с таким id нет.");
        }
        switch (state) {
            case "ALL": {
                return bookingRepository.findByItemOwnerIdOrderByStartDesc(userId).stream()
                        .map(BookingMapper::toBookingForSend)
                        .collect(Collectors.toList());
            }
            case "CURRENT": {
                return bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                                LocalDateTime.now(), LocalDateTime.now()).stream()
                        .map(BookingMapper::toBookingForSend)
                        .collect(Collectors.toList());
            }
            case "PAST": {
                return bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingForSend)
                        .collect(Collectors.toList());
            }
            case "FUTURE": {
                return bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingForSend)
                        .collect(Collectors.toList());
            }
            case "WAITING":
                return bookingRepository.findByItemOwnerIdAndStatus(userId, Status.WAITING)
                        .stream()
                        .map(BookingMapper::toBookingForSend)
                        .collect(Collectors.toList());
            case "REJECTED": {
                return bookingRepository.findByItemOwnerIdAndStatus(userId, Status.REJECTED)
                        .stream()
                        .map(BookingMapper::toBookingForSend)
                        .collect(Collectors.toList());
            }
            default: {
                throw new StatusException("Статус бронирования неверный");
            }
        }
    }
}
