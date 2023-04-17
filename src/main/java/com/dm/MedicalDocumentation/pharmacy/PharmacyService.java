package com.dm.MedicalDocumentation.pharmacy;

import com.dm.MedicalDocumentation.city.City;
import com.dm.MedicalDocumentation.city.CityRepository;
import com.dm.MedicalDocumentation.user.User;
import com.dm.MedicalDocumentation.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PharmacyService {
    private final PharmacyRepository repository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;

    public boolean recordExists(PharmacyRequest request) {
        Optional<Pharmacy> pharmacy = repository.findByPharmacyNameAndCityZipCodeAndAddress(request.getName(),
                request.getZipCode(), request.getAddress());
        Optional<User> user = userRepository.findByUserLogin(request.getUserLogin());
        Pharmacy foundPharmacy = null;
        if (user.isPresent()) {
            foundPharmacy = user.get().getPharmacy();
        }
        return pharmacy.isPresent() || foundPharmacy != null;
    }

    public void createPharmacy(PharmacyRequest request) {
        User user = userRepository.findByUserLogin(request.getUserLogin())
                .orElseThrow(() -> new IllegalArgumentException("No user with login " + request.getUserLogin() + " exists."));
        City city = cityRepository.findById(request.getZipCode())
                .orElseThrow(() -> new IllegalArgumentException("No city with zipCode " + request.getZipCode() + " exists."));
        Pharmacy pharmacy = Pharmacy.builder()
                .pharmacyName(request.getName())
                .city(city)
                .address(request.getAddress())
                .user(user)
                .build();
        repository.save(pharmacy);
    }

    public Long getPharmacyCount(String cityName) {
        if (!cityName.isBlank()) {
            return repository.countByCityCityName(cityName);
        }
        return repository.count();
    }
}
