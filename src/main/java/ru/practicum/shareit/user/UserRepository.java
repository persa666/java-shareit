package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User AS u SET u.name = ?1, u.email = ?2 WHERE u.id = ?3")
    void saveUserById(String name, String email, int userId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User AS u SET u.name = ?1 WHERE u.id = ?2")
    void saveUserNameById(String name, int userId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User AS u SET u.email = ?1 WHERE u.id = ?2")
    void saveUserEmailById(String email, int userId);

    @Query("SELECT COUNT(u) FROM User AS u WHERE u.id = ?1")
    Integer countUserById(Integer userId);
}
