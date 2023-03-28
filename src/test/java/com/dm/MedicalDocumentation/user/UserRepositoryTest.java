package com.dm.MedicalDocumentation.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserRepository underTest;
    @Test
    void getNewUserCountByMonth() {
        User user1 = User.builder().userLogin("user1").email("a").password("a").telephone("")
                .role(Role.DOCTOR).createdAt(LocalDateTime.of(2022, 4, 5, 8, 12)).build();
        User user2 = User.builder().userLogin("user2").email("a").password("a").telephone("")
                .role(Role.DOCTOR).createdAt(LocalDateTime.of(2022, 5, 5, 8, 12)).build();
        User user3 = User.builder().userLogin("user3").email("a").password("a").telephone("")
                .role(Role.DOCTOR).createdAt(LocalDateTime.of(2022, 6, 5, 8, 12)).build();
        User user4 = User.builder().userLogin("user4").email("a").password("a").telephone("")
                .role(Role.DOCTOR).createdAt(LocalDateTime.of(2022, 7, 5, 8, 12)).build();
        User user5 = User.builder().userLogin("user5").email("a").password("a").telephone("")
                .role(Role.DOCTOR).createdAt(LocalDateTime.of(2022, 8, 5, 8, 12)).build();
        User user6 = User.builder().userLogin("user6").email("a").password("a").telephone("")
                .role(Role.DOCTOR).createdAt(LocalDateTime.of(2022, 9, 5, 8, 12)).build();
        User user7 = User.builder().userLogin("user7").email("a").password("a").telephone("")
                .role(Role.DOCTOR).createdAt(LocalDateTime.of(2022, 10, 5, 8, 12)).build();
        User user8 = User.builder().userLogin("user8").email("a").password("a").telephone("")
                .role(Role.DOCTOR).createdAt(LocalDateTime.of(2022, 11, 5, 8, 12)).build();
        User user9 = User.builder().userLogin("user9").email("a").password("a").telephone("")
                .role(Role.DOCTOR).createdAt(LocalDateTime.of(2022, 12, 5, 8, 12)).build();
        User user10 = User.builder().userLogin("user10").email("a").password("a").telephone("")
                .role(Role.DOCTOR).createdAt(LocalDateTime.of(2023, 1, 5, 8, 12)).build();
        User user11 = User.builder().userLogin("user11").email("a").password("a").telephone("")
                .role(Role.DOCTOR).createdAt(LocalDateTime.of(2023, 2, 5, 8, 12)).build();
        User user12 = User.builder().userLogin("user12").email("a").password("a").telephone("")
                .role(Role.DOCTOR).createdAt(LocalDateTime.of(2023, 3, 5, 8, 12)).build();
        User user13 = User.builder().userLogin("user13").email("a").password("a").telephone("")
                .role(Role.DOCTOR).createdAt(LocalDateTime.of(2023, 3, 5, 8, 12)).build();
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.persist(user4);
        entityManager.persist(user5);
        entityManager.persist(user6);
        entityManager.persist(user7);
        entityManager.persist(user8);
        entityManager.persist(user9);
        entityManager.persist(user10);
        entityManager.persist(user11);
        entityManager.persist(user12);
        entityManager.persist(user13);

        List<Object[]> newUsersByMonth = underTest.getNewUserCountByMonth(
                LocalDateTime.of(2022, 4, 1, 0, 0),
                LocalDateTime.of(2023, 3, 31, 23, 59));
        Object[] counts = newUsersByMonth.stream().map(row -> row[0]).toArray();
        assertArrayEquals(new Long[]{1L, 1L, 2L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L}, counts);
    }
}