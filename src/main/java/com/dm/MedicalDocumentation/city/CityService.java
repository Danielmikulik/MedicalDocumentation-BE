package com.dm.MedicalDocumentation.city;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository repository;
    public boolean recordExists(String id) {
        Optional<City> city = repository.findById(id);
        return city.isPresent();
    }

    public void createCity(CityRequest request) {
        City city = City.builder()
                .zipCode(request.getZipCode())
                .cityName(request.getName())
                .build();
        repository.save(city);
    }

    public List<String> getCities() {
        List<City> cities = repository.findAll();
        List<String> result = new ArrayList<>(cities.size());
        for (City city : cities) {
            result.add(city.getZipCode() + " " + city.getCityName());
        }
        return result;
    }
}
