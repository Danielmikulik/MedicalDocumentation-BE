package com.dm.MedicalDocumentation.person;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByBirthNumber(String birthNumber);
}
