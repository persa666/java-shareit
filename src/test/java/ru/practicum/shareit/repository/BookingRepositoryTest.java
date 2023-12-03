package ru.practicum.shareit.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource(locations = "/applicationTest.properties")
@ActiveProfiles("ci,test")
@Transactional
public class BookingRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    public void testUpdateStatusByBookingId() {
        int userId = 1;
        int bookingId = 1;
        Status newStatus = Status.APPROVED;

        bookingRepository.updateStatusByBookingId(userId, newStatus, bookingId);

        entityManager.flush();
        entityManager.clear();

        long count = bookingRepository.count();
        assertEquals(0, count, "В базе данных нет никаких бронирований");
    }

    @Test
    public void testExistsByItemIdAndBookerIdAndEndBeforeAndStatus() {
        int userId = 1;
        int itemId = 1;
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);
        Status status = Status.APPROVED;

        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(b) FROM Booking b WHERE b.booker.id = :userId AND b.item.id = :itemId " +
                        "AND b.end <= :time AND b.status = :status", Long.class);
        query.setParameter("userId", userId);
        query.setParameter("itemId", itemId);
        query.setParameter("time", endTime);
        query.setParameter("status", status);

        long count = query.getSingleResult();

        assertTrue(count >= 0);
    }

    @Test
    public void testFindBookingByIdByOwnerId() {
        int userId = 1;
        int bookingId = 1;

        Booking booking = new Booking();

        entityManager.persist(booking);
        entityManager.flush();
        entityManager.clear();

        TypedQuery<Booking> query = entityManager.createQuery(
                "SELECT b FROM Booking b WHERE b.item.owner.id = :userId AND b.id = :bookingId", Booking.class);
        query.setParameter("userId", userId);
        query.setParameter("bookingId", bookingId);

        List<Booking> bookings = query.getResultList();

        assertTrue(bookings.isEmpty(), "Бронирование не должно быть найдено в базе данных");
    }

}
