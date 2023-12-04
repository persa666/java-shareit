package ru.practicum.shareit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForSend;
import ru.practicum.shareit.exp.BookingException;
import ru.practicum.shareit.exp.NonExistentBookingException;
import ru.practicum.shareit.exp.NonExistentUserException;
import ru.practicum.shareit.exp.StatusException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void createBookingExceptionTest() {
        BookingDto bookingDto = new BookingDto(5, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        int userId = 1;
        assertThrows(NonExistentBookingException.class, () -> bookingService.createBooking(bookingDto, userId));

        verifyNoInteractions(userRepository);
        verifyNoInteractions(bookingRepository);
    }

    @Test
    void createBookingExceptionDateTest() {
        BookingDto bookingDto = new BookingDto(5, LocalDateTime.now(), LocalDateTime.now().minusDays(5));
        int userId = 1;
        assertThrows(BookingException.class, () -> bookingService.createBooking(bookingDto, userId));

        verifyNoInteractions(itemRepository);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(bookingRepository);
    }

    @Test
    void createBookingExistentUserTest() {
        BookingDto bookingDto = new BookingDto(5, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        int userId = 1;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setOwner(user);
        when(itemRepository.findById(any(Integer.class))).thenReturn(Optional.of(item));
        assertThrows(NonExistentUserException.class, () -> bookingService.createBooking(bookingDto, userId));

        verify(itemRepository, times(1)).findById(any(Integer.class));
        verifyNoInteractions(userRepository);
        verifyNoInteractions(bookingRepository);
    }

    @Test
    void createBookingExistentAvailableTest() {
        BookingDto bookingDto = new BookingDto(5, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        int userId = 1;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setOwner(user);
        item.setAvailable(false);
        when(itemRepository.findById(any(Integer.class))).thenReturn(Optional.of(item));
        assertThrows(BookingException.class, () -> bookingService.createBooking(bookingDto, userId + 1));

        verify(itemRepository, times(1)).findById(any(Integer.class));
        verifyNoInteractions(userRepository);
        verifyNoInteractions(bookingRepository);
    }

    @Test
    void createBookingUserExceptionTest() {
        BookingDto bookingDto = new BookingDto(5, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        int userId = 1;
        int secondUserId = 2;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setOwner(user);
        item.setAvailable(true);
        when(itemRepository.findById(any(Integer.class))).thenReturn(Optional.of(item));
        when(userRepository.findById(secondUserId))
                .thenThrow(new NonExistentUserException("Пользователя с таким id нет."));
        assertThrows(NonExistentUserException.class, () -> bookingService.createBooking(bookingDto, secondUserId));

        verify(itemRepository, times(1)).findById(any(Integer.class));
        verify(userRepository, times(1)).findById(secondUserId);
        verifyNoInteractions(bookingRepository);
    }

    @Test
    void createBookingTest() {
        BookingDto bookingDto = new BookingDto(5, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        int userId = 1;
        int secondUserId = 2;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setName("name");
        item.setOwner(user);
        item.setAvailable(true);
        User booker = new User();
        booker.setId(secondUserId);
        when(itemRepository.findById(any(Integer.class))).thenReturn(Optional.of(item));
        when(userRepository.findById(secondUserId))
                .thenReturn(Optional.of(booker));
        Booking booking = new Booking(1, LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                item, booker, Status.APPROVED);
        when(bookingRepository.findByItemIdAndStartAndEnd(any(Integer.class), any(LocalDateTime.class),
                any(LocalDateTime.class))).thenReturn(booking);
        BookingDtoForSend createdBooking = bookingService.createBooking(bookingDto, secondUserId);

        assertNotNull(createdBooking, "Созданная бронь не должна быть null");
        assertEquals(Status.APPROVED, createdBooking.getStatus(), "Статус брони должен быть APPROVED");

        verify(itemRepository, times(1)).findById(any(Integer.class));
        verify(userRepository, times(1)).findById(secondUserId);
        verify(bookingRepository, times(1)).findByItemIdAndStartAndEnd(
                any(Integer.class), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void replaceBookingExceptionTest() {
        int userId = 1;
        boolean approved = true;
        int bookingId = 1;
        Booking booking = new Booking(1, LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                new Item(), new User(), Status.APPROVED);
        when(bookingRepository.findByIdAndStatus(any(Integer.class), any(Status.class))).thenReturn(booking);

        assertThrows(BookingException.class, () -> bookingService.replaceBooking(userId, approved, bookingId));

        verify(bookingRepository, times(1)).findByIdAndStatus(any(Integer.class), any(Status.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void replaceBookingNotOwnerTest() {
        int userId = 1;
        boolean approved = false;
        int bookingId = 1;
        when(bookingRepository.updateStatusByBookingId(userId, Status.REJECTED, bookingId)).thenReturn(0);

        assertThrows(BookingException.class, () -> bookingService.replaceBooking(userId, approved, bookingId));

        verify(bookingRepository, times(1)).updateStatusByBookingId(userId, Status.REJECTED, bookingId);
    }

    @Test
    void replaceBookingNonExistentUserTest() {
        int userId = 1;
        boolean approved = true;
        int bookingId = 1;
        when(bookingRepository.updateStatusByBookingId(userId, Status.APPROVED, bookingId)).thenReturn(0);

        assertThrows(NonExistentUserException.class, () -> bookingService.replaceBooking(userId, approved, bookingId));

        verify(bookingRepository, times(1)).updateStatusByBookingId(userId, Status.APPROVED, bookingId);
    }

    @Test
    void replaceBookingTest() {
        int userId = 1;
        boolean approved = true;
        int bookingId = 1;
        when(bookingRepository.updateStatusByBookingId(userId, Status.APPROVED, bookingId)).thenReturn(1);
        int secondUserId = 2;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setName("name");
        item.setOwner(user);
        item.setAvailable(true);
        User booker = new User();
        booker.setId(secondUserId);
        Booking booking = new Booking(1, LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                item, booker, Status.APPROVED);
        when(bookingRepository.findById(any(Integer.class))).thenReturn(Optional.of(booking));
        BookingDtoForSend updatedBooking = bookingService.replaceBooking(userId, approved, bookingId);

        assertNotNull(updatedBooking, "Booking should not be null after replacement");
        assertEquals(Status.APPROVED, updatedBooking.getStatus(),
                "Booking status should be updated to APPROVED");

        verify(bookingRepository, times(1)).updateStatusByBookingId(userId, Status.APPROVED, bookingId);
        verify(bookingRepository, times(1)).findById(any(Integer.class));
    }

    @Test
    void replaceBookingNonExistentBookingExceptionTest() {
        int userId = 1;
        boolean approved = true;
        int bookingId = 1;
        when(bookingRepository.updateStatusByBookingId(userId, Status.APPROVED, bookingId)).thenReturn(1);
        int secondUserId = 2;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setName("name");
        item.setOwner(user);
        item.setAvailable(true);
        User booker = new User();
        booker.setId(secondUserId);
        Booking booking = new Booking(1, LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                item, booker, Status.APPROVED);
        assertThrows(NonExistentBookingException.class,
                () -> bookingService.replaceBooking(userId, approved, bookingId));

        verify(bookingRepository, times(1))
                .updateStatusByBookingId(userId, Status.APPROVED, bookingId);
    }

    @Test
    void findBookingsByUserIdNonExistentUserExceptionTest() {
        int userId = 1;
        String state = "ALL";
        int from = 0;
        int size = 1;
        when(userRepository.countUserById(any(Integer.class))).thenReturn(0);

        assertThrows(NonExistentUserException.class,
                () -> bookingService.findBookingsByUserId(userId, state, PageRequest.of(from / size, size)));
    }

    @Test
    void findBookingsByUserIdAllTest() {
        int userId = 1;
        String state = "ALL";
        int from = 0;
        int size = 1;
        List<Booking> list = new ArrayList<>();
        Page<Booking> page = new PageImpl<>(list);
        when(userRepository.countUserById(any(Integer.class))).thenReturn(1);
        when(bookingRepository.findByBookerIdOrderByStartDesc(any(Integer.class), any(Pageable.class)))
                .thenReturn(page);

        List<BookingDtoForSend> bookings = bookingService.findBookingsByUserId(userId, state,
                PageRequest.of(from / size, size));

        assertNotNull(bookings, "List of bookings should not be null");
        assertTrue(bookings.isEmpty(), "List of bookings should be empty");

        verify(userRepository, times(1)).countUserById(userId);
        verify(bookingRepository, times(1)).findByBookerIdOrderByStartDesc(userId, PageRequest.of(from, size));
    }

    @Test
    void findBookingsByUserIdCurrentTest() {
        int userId = 1;
        String state = "CURRENT";
        int from = 0;
        int size = 1;
        List<Booking> list = new ArrayList<>();
        Page<Booking> page = new PageImpl<>(list);
        when(userRepository.countUserById(any(Integer.class))).thenReturn(1);
        when(bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(any(Integer.class),
                any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(page);

        List<BookingDtoForSend> bookings = bookingService.findBookingsByUserId(userId, state,
                PageRequest.of(from / size, size));

        assertNotNull(bookings, "List of bookings should not be null");
        assertTrue(bookings.isEmpty(), "List of bookings should be empty");

        verify(userRepository, times(1)).countUserById(userId);
    }

    @Test
    void findBookingsByUserIdPastTest() {
        int userId = 1;
        String state = "PAST";
        int from = 0;
        int size = 1;
        List<Booking> list = new ArrayList<>();
        Page<Booking> page = new PageImpl<>(list);
        when(userRepository.countUserById(any(Integer.class))).thenReturn(1);
        when(bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(any(Integer.class),
                any(LocalDateTime.class), any(Pageable.class))).thenReturn(page);

        List<BookingDtoForSend> bookings = bookingService.findBookingsByUserId(userId, state,
                PageRequest.of(from / size, size));

        assertNotNull(bookings, "List of bookings should not be null");
        assertTrue(bookings.isEmpty(), "List of bookings should be empty");

        verify(userRepository, times(1)).countUserById(userId);
    }

    @Test
    void findBookingsByUserIdFutureTest() {
        int userId = 1;
        String state = "FUTURE";
        int from = 0;
        int size = 1;
        List<Booking> list = new ArrayList<>();
        Page<Booking> page = new PageImpl<>(list);
        when(userRepository.countUserById(any(Integer.class))).thenReturn(1);
        when(bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(any(Integer.class),
                any(LocalDateTime.class), any(Pageable.class))).thenReturn(page);

        List<BookingDtoForSend> bookings = bookingService.findBookingsByUserId(userId, state,
                PageRequest.of(from / size, size));

        assertNotNull(bookings, "List of bookings should not be null");
        assertTrue(bookings.isEmpty(), "List of bookings should be empty");

        verify(userRepository, times(1)).countUserById(userId);
    }

    @Test
    void findBookingsByUserIdWaitingTest() {
        int userId = 1;
        String state = "WAITING";
        int from = 0;
        int size = 1;
        List<Booking> list = new ArrayList<>();
        Page<Booking> page = new PageImpl<>(list);
        when(userRepository.countUserById(any(Integer.class))).thenReturn(1);
        when(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(any(Integer.class), any(Status.class),
                any(Pageable.class))).thenReturn(page);

        List<BookingDtoForSend> bookings = bookingService.findBookingsByUserId(userId, state,
                PageRequest.of(from / size, size));

        assertNotNull(bookings, "List of bookings should not be null");
        assertTrue(bookings.isEmpty(), "List of bookings should be empty");

        verify(userRepository, times(1)).countUserById(userId);
    }

    @Test
    void findBookingsByUserIdRejectedTest() {
        int userId = 1;
        String state = "REJECTED";
        int from = 0;
        int size = 1;
        List<Booking> list = new ArrayList<>();
        Page<Booking> page = new PageImpl<>(list);
        when(userRepository.countUserById(any(Integer.class))).thenReturn(1);
        when(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(any(Integer.class), any(Status.class),
                any(Pageable.class))).thenReturn(page);

        List<BookingDtoForSend> bookings = bookingService.findBookingsByUserId(userId, state,
                PageRequest.of(from / size, size));

        assertNotNull(bookings, "List of bookings should not be null");
        assertTrue(bookings.isEmpty(), "List of bookings should be empty");

        verify(userRepository, times(1)).countUserById(userId);
    }

    @Test
    void findBookingsByUserIdNotCorrectlyTest() {
        int userId = 1;
        String state = "123";
        int from = 0;
        int size = 1;
        when(userRepository.countUserById(any(Integer.class))).thenReturn(1);

        assertThrows(StatusException.class,
                () -> bookingService.findBookingsByUserId(userId, state, PageRequest.of(from / size, size)));

        verify(userRepository, times(1)).countUserById(userId);
    }

    @Test
    void findBookingForItemsByUserNonExistentIdTest() {
        int userId = 1;
        String state = "ALL";
        int from = 0;
        int size = 1;
        when(userRepository.countUserById(any(Integer.class))).thenReturn(0);

        assertThrows(NonExistentUserException.class,
                () -> bookingService.findBookingForItemsByUserId(userId, state, PageRequest.of(from / size, size)));
    }

    @Test
    void findBookingForItemsByUserIdAllTest() {
        int userId = 1;
        String state = "ALL";
        int from = 0;
        int size = 1;
        List<Booking> list = new ArrayList<>();
        Page<Booking> page = new PageImpl<>(list);
        when(userRepository.countUserById(any(Integer.class))).thenReturn(1);
        when(bookingRepository.findByItemOwnerIdOrderByStartDesc(any(Integer.class), any(Pageable.class)))
                .thenReturn(page);

        List<BookingDtoForSend> bookings = bookingService.findBookingForItemsByUserId(userId, state,
                PageRequest.of(from / size, size));

        assertNotNull(bookings, "List of bookings should not be null");
        assertTrue(bookings.isEmpty(), "List of bookings should be empty");

        verify(userRepository, times(1)).countUserById(userId);
        verify(bookingRepository, times(1)).findByItemOwnerIdOrderByStartDesc(userId, PageRequest.of(from, size));
    }

    @Test
    void findBookingForItemsByUserIdCurrentTest() {
        int userId = 1;
        String state = "CURRENT";
        int from = 0;
        int size = 1;
        List<Booking> list = new ArrayList<>();
        Page<Booking> page = new PageImpl<>(list);
        when(userRepository.countUserById(any(Integer.class))).thenReturn(1);
        when(bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(any(Integer.class),
                any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(page);

        List<BookingDtoForSend> bookings = bookingService.findBookingForItemsByUserId(userId, state,
                PageRequest.of(from / size, size));

        assertNotNull(bookings, "List of bookings should not be null");
        assertTrue(bookings.isEmpty(), "List of bookings should be empty");

        verify(userRepository, times(1)).countUserById(userId);
    }

    @Test
    void findBookingForItemsByUserIdPastTest() {
        int userId = 1;
        String state = "PAST";
        int from = 0;
        int size = 1;
        List<Booking> list = new ArrayList<>();
        Page<Booking> page = new PageImpl<>(list);
        when(userRepository.countUserById(any(Integer.class))).thenReturn(1);
        when(bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(any(Integer.class),
                any(LocalDateTime.class), any(Pageable.class))).thenReturn(page);

        List<BookingDtoForSend> bookings = bookingService.findBookingForItemsByUserId(userId, state,
                PageRequest.of(from / size, size));

        assertNotNull(bookings, "List of bookings should not be null");
        assertTrue(bookings.isEmpty(), "List of bookings should be empty");

        verify(userRepository, times(1)).countUserById(userId);
    }

    @Test
    void findBookingForItemsByUserIdFutureTest() {
        int userId = 1;
        String state = "FUTURE";
        int from = 0;
        int size = 1;
        List<Booking> list = new ArrayList<>();
        Page<Booking> page = new PageImpl<>(list);
        when(userRepository.countUserById(any(Integer.class))).thenReturn(1);
        when(bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(any(Integer.class),
                any(LocalDateTime.class), any(Pageable.class))).thenReturn(page);

        List<BookingDtoForSend> bookings = bookingService.findBookingForItemsByUserId(userId, state,
                PageRequest.of(from / size, size));

        assertNotNull(bookings, "List of bookings should not be null");
        assertTrue(bookings.isEmpty(), "List of bookings should be empty");

        verify(userRepository, times(1)).countUserById(userId);
    }

    @Test
    void findBookingForItemsByUserIdWaitingTest() {
        int userId = 1;
        String state = "WAITING";
        int from = 0;
        int size = 1;
        List<Booking> list = new ArrayList<>();
        Page<Booking> page = new PageImpl<>(list);
        when(userRepository.countUserById(any(Integer.class))).thenReturn(1);
        when(bookingRepository.findByItemOwnerIdAndStatus(any(Integer.class), any(Status.class),
                any(Pageable.class))).thenReturn(page);

        List<BookingDtoForSend> bookings = bookingService.findBookingForItemsByUserId(userId, state,
                PageRequest.of(from / size, size));

        assertNotNull(bookings, "List of bookings should not be null");
        assertTrue(bookings.isEmpty(), "List of bookings should be empty");

        verify(userRepository, times(1)).countUserById(userId);
    }

    @Test
    void findBookingForItemsByUserIdRejectedTest() {
        int userId = 1;
        String state = "REJECTED";
        int from = 0;
        int size = 1;
        List<Booking> list = new ArrayList<>();
        Page<Booking> page = new PageImpl<>(list);
        when(userRepository.countUserById(any(Integer.class))).thenReturn(1);
        when(bookingRepository.findByItemOwnerIdAndStatus(any(Integer.class), any(Status.class),
                any(Pageable.class))).thenReturn(page);

        List<BookingDtoForSend> bookings = bookingService.findBookingForItemsByUserId(userId, state,
                PageRequest.of(from / size, size));

        assertNotNull(bookings, "List of bookings should not be null");
        assertTrue(bookings.isEmpty(), "List of bookings should be empty");

        verify(userRepository, times(1)).countUserById(userId);
    }

    @Test
    void findBookingForItemsByUserIdNotCorrectlyTest() {
        int userId = 1;
        String state = "123";
        int from = 0;
        int size = 1;
        when(userRepository.countUserById(any(Integer.class))).thenReturn(1);

        assertThrows(StatusException.class,
                () -> bookingService.findBookingForItemsByUserId(userId, state, PageRequest.of(from / size, size)));

        verify(userRepository, times(1)).countUserById(userId);
    }

}
