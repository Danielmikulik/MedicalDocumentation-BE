package com.dm.MedicalDocumentation.city;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityRepository extends JpaRepository<City, String> {
    @Query("SELECT DISTINCT c.cityName " +
            "FROM City c " +
            "ORDER BY c.cityName")
    List<String> getDistinctCityNames();
}
