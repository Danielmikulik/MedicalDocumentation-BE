package com.dm.MedicalDocumentation.city;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CityRepositoryTest {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private CityRepository underTest;

    @Test
    void findCityById() {
        City city = City.builder()
                .zipCode("010 01")
                .cityName("Žilina")
                .build();
        entityManager.persist(city);
        Optional<City> foundCity = underTest.findById(city.getZipCode());
        assertEquals(foundCity.get(), city);
    }
}