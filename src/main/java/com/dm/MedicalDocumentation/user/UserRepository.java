package com.dm.MedicalDocumentation.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserLogin(String userLogin);
    List<User> findAllByOrderByUserLogin();

    @Query("SELECT u.userLogin " +
            "FROM User u " +
            "WHERE u.role = 'PHARMACY' AND u NOT IN(" +
            "SELECT p.user FROM Pharmacy p)")
    List<String> findUnusedPharmacyLogins();

    @Query("SELECT u.userLogin " +
            "FROM User u " +
            "WHERE u.role = 'PATIENT' AND u NOT IN(" +
            "SELECT p.user FROM Patient p)")
    List<String> findUnusedPatientLogins();

    @Query("SELECT u.userLogin " +
            "FROM User u " +
            "WHERE u.role = 'DOCTOR' AND u NOT IN(" +
            "SELECT d.user FROM Doctor d)")
    List<String> findUnusedDoctorLogins();

    @Query("SELECT COUNT(u), MONTH(u.createdAt), YEAR(u.createdAt) " +
            "FROM User u " +
            "WHERE u.createdAt BETWEEN ?1 AND ?2 " +
            "GROUP BY MONTH(u.createdAt), YEAR(u.createdAt)")
    List<Object[]> getNewUserCountByMonth(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(u), WEEK(u.createdAt), YEAR(u.createdAt) " +
            "FROM User u " +
            "WHERE u.createdAt BETWEEN ?1 AND ?2 " +
            "GROUP BY WEEK(u.createdAt), YEAR(u.createdAt)")
    List<Object[]> getNewUserCountByWeek(LocalDateTime startDate, LocalDateTime endDate);
}
