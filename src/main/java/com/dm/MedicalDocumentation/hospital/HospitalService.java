package com.dm.MedicalDocumentation.hospital;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HospitalService {
    private final HospitalRepository repository;
    public boolean recordExists(String name) {
        Optional<Hospital> hospital = repository.findByHospitalName(name);
        return hospital.isPresent();
    }

    public void createHospital(String name) {
        Hospital hospital = Hospital.builder()
                .hospitalName(name)
                .build();
        repository.save(hospital);
    }

    public List<String> getHospitals() {
        List<Hospital> hospitals = repository.findAll();
        List<String> result = new ArrayList<>(hospitals.size());
        for (Hospital hospital : hospitals) {
            result.add(hospital.getHospitalName());
        }
        return result;
    }
}
