package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE User AS u SET u.name = :name, u.email = :email WHERE u.id = :userId")
    void saveUserById(@Param("name") String name, @Param("email") String email, @Param("userId") int userId);

    @Modifying
    @Transactional
    @Query("UPDATE User AS u SET u.name = :name WHERE u.id = :userId")
    void saveUserNameById(@Param("name") String name, @Param("userId") int userId);

    @Modifying
    @Transactional
    @Query("UPDATE User AS u SET u.email = :email WHERE u.id = :userId")
    void saveUserEmailById(@Param("email") String email, @Param("userId") int userId);

    @Query("SELECT COUNT(u) FROM User AS u WHERE u.id = :userId")
    Integer countUserById(@Param("userId") Integer userId);
}
