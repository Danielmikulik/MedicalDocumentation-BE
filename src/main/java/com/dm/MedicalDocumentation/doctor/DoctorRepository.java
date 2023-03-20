package com.dm.MedicalDocumentation.doctor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserUserLogin(String userLogin);
    Optional<Doctor> findByPersonBirthNumber(String birthNumber);
}
