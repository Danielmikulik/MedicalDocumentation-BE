package com.dm.MedicalDocumentation.patient;

import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.doctor.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository repository;
    private final DoctorRepository doctorRepository;

    public String getPatientNameByBirthNumber(String birthNumber) {
        Optional<Patient> patient = repository.findByPersonBirthNumber(birthNumber);
        return patient.map(value -> value.getPerson().getFullName()).orElse(null);
    }

    public List<String> getDoctorsPatients(String userLogin, boolean isGeneralPractitioner) {
        Doctor doctor = doctorRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new IllegalArgumentException("No doctor with given login found!"));
        List<Patient> patients = isGeneralPractitioner
                ? repository.findGeneralPractitionersPatients(doctor)
                : repository.findDoctorsPatients(doctor.getDoctorId());
        List<String> result = new ArrayList<>(patients.size());
        for (Patient patient : patients) {
            result.add(patient.getPerson().getBirthNumber() + " " + patient.getPerson().getFullName());
        }
        return result;
    }
}
