package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Booking findByItemIdAndStartAndEnd(int itemId, LocalDateTime start, LocalDateTime end);

    @Modifying
    @Transactional
    @Query("UPDATE Booking AS b SET b.status = :status WHERE b.item.id IN (SELECT i.id FROM Item AS i " +
            "WHERE i.owner.id = :userId) AND b.id = :bookingId")
    int updateStatusByBookingId(@Param("userId") int userId, @Param("status") Status status,
                                @Param("bookingId") int bookingId);

    @Query("SELECT COUNT(b) FROM Booking AS b WHERE b.booker.id = :userId AND b.item.id = :itemId AND b.end <= :time " +
            "AND b.status = :status")
    int existsByItemIdAndBookerIdAndEndBeforeAndStatus(@Param("userId") int userId, @Param("itemId") int itemId,
                                                       @Param("time") LocalDateTime time,
                                                       @Param("status") Status status);

    Booking findFirstByItemIdAndItemOwnerIdAndStartAfterAndStatusOrderByStartAsc(int itemId, int userId,
                                                                                 LocalDateTime time, Status status);

    Booking findFirstByItemIdAndItemOwnerIdAndStartBeforeAndStatusOrderByEndDesc(int itemId, int userId,
                                                                                 LocalDateTime time, Status status);

    Booking findByIdAndStatus(int bookingId, Status status);

    @Query("SELECT b FROM Booking AS b WHERE b.id = :bookingId AND (b.booker.id = :userId " +
            "OR (b.item.owner.id = :userId))")
    Booking findBookingByIdByOwnerId(@Param("userId") int userId, @Param("bookingId") int bookingId);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(int userId, Status state);

    List<Booking> findByBookerIdOrderByStartDesc(int userId);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(int userId, LocalDateTime time);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(int userId, LocalDateTime time);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(int userId, LocalDateTime after,
                                                                          LocalDateTime before);

    List<Booking> findByItemOwnerIdAndStatus(int userId, Status status);

    List<Booking> findByItemOwnerIdOrderByStartDesc(int userId);

    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(int userId, LocalDateTime after,
                                                                             LocalDateTime before);

    List<Booking> findByItemOwnerIdAndStartAfterOrderByStartDesc(int userId, LocalDateTime time);

    List<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(int userId, LocalDateTime time);

}