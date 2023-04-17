package com.dm.MedicalDocumentation.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByBirthNumber(String birthNumber);

    @Query("SELECT per " +
            "FROM Person per " +
            "WHERE per NOT IN(SELECT pat.person FROM Patient pat) " +
            "AND per NOT IN(SELECT doc.person FROM Doctor doc)")
    List<Person> getUnassignedPeople();

    @Query("SELECT per " +
            "FROM Person per " +
            "WHERE per NOT IN(SELECT doc.person FROM Doctor doc)")
    List<Person> getUnassignedDoctors();
}
