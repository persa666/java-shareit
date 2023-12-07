package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

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

    Page<Booking> findByBookerIdAndStatusOrderByStartDesc(int userId, Status state, Pageable pageable);

    Page<Booking> findByBookerIdOrderByStartDesc(int userId, Pageable pageable);

    Page<Booking> findByBookerIdAndStartAfterOrderByStartDesc(int userId, LocalDateTime time, Pageable pageable);

    Page<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(int userId, LocalDateTime time, Pageable pageable);

    Page<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(int userId, LocalDateTime after,
                                                                          LocalDateTime before, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStatus(int userId, Status status, Pageable pageable);

    Page<Booking> findByItemOwnerIdOrderByStartDesc(int userId, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(int userId, LocalDateTime after,
                                                                             LocalDateTime before, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStartAfterOrderByStartDesc(int userId, LocalDateTime time, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(int userId, LocalDateTime time, Pageable pageable);

}
