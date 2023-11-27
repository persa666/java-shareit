package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "INSERT INTO bookings (start_date, end_date, item_id, booker_id, status) " +
            "SELECT ?2, ?3, ?1, ?4, ?5 FROM items WHERE id = ?1 AND is_available = true", nativeQuery = true)
    int saveByItemId(int itemId, LocalDateTime start, LocalDateTime end, int userId, String status);

    @Query(value = "SELECT b FROM Booking AS b WHERE b.item.id = ?1 AND b.start = ?2 AND b.end = ?3")
    Booking findByItemIdAndStartAndEnd(int itemId, LocalDateTime start, LocalDateTime end);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Booking AS b SET b.status = ?2 WHERE b.item.id IN (SELECT i.id FROM Item AS i " +
            "WHERE i.owner.id = ?1) AND b.id = ?3")
    int updateStatusByBookingId(int userId, Status status, int bookingId);

    @Query("SELECT COUNT(b) FROM Booking AS b WHERE b.booker.id = ?1 AND b.item.id = ?2 AND b.end <= ?3 " +
            "AND b.status = ?4")
    int existsByItemIdAndBookerIdAndEndBeforeAndStatus(int userId, int itemId, LocalDateTime time, Status status);

    Booking findById(int bookingId);

    Booking findFirstByItemIdAndItemOwnerIdAndStartAfterAndStatusOrderByStartAsc(int itemId, int userId,
                                                                                 LocalDateTime time, Status status);

    Booking findFirstByItemIdAndItemOwnerIdAndStartBeforeAndStatusOrderByEndDesc(int itemId, int userId,
                                                                                 LocalDateTime time, Status status);

    Booking findByIdAndStatus(int bookingId, Status status);

    @Query("SELECT b FROM Booking AS b WHERE b.id = ?2 AND (b.booker.id = ?1 OR (b.item.owner.id = ?1))")
    Booking findBookingByIdByOwnerId(int userId, int bookingId);

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
