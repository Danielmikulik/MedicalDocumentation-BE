package com.dm.MedicalDocumentation.pharmacy;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
    Optional<Pharmacy> findByPharmacyNameAndCityZipCodeAndAddress(String pharmacyName, String zipCode, String address);
    long countByCityCityName (String cityName);
}
