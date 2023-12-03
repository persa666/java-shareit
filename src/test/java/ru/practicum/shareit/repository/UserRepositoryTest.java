package ru.practicum.shareit.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "/applicationTest.properties")
@ActiveProfiles("test")
@Transactional
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testSaveUserById() {
        User user = new User();
        user.setName("TestName");
        user.setEmail("test@example.com");
        user = userRepository.save(user);

        userRepository.saveUserById("NewName", "new@example.com", user.getId());
        testEntityManager.flush();
        testEntityManager.clear();

        User updatedUser = userRepository.findById(user.getId()).orElse(null);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo("NewName");
        assertThat(updatedUser.getEmail()).isEqualTo("new@example.com");
    }

    @Test
    public void testSaveUserNameById() {
        User user = new User();
        user.setName("TestName");
        user.setEmail("test@example.com");
        user = userRepository.save(user);

        userRepository.saveUserNameById("NewName", user.getId());
        testEntityManager.flush();
        testEntityManager.clear();

        User updatedUser = userRepository.findById(user.getId()).orElse(null);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo("NewName");
    }

    @Test
    public void testSaveUserEmailById() {
        User user = new User();
        user.setName("TestName");
        user.setEmail("test@example.com");
        user = userRepository.save(user);

        userRepository.saveUserEmailById("new@example.com", user.getId());
        testEntityManager.flush();
        testEntityManager.clear();

        User updatedUser = userRepository.findById(user.getId()).orElse(null);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getEmail()).isEqualTo("new@example.com");
    }

    @Test
    public void testCountUserById() {
        User user = new User();
        user.setName("TestName");
        user.setEmail("test@example.com");
        user = userRepository.save(user);

        Integer count = userRepository.countUserById(user.getId());

        assertThat(count).isEqualTo(1);
    }
}
