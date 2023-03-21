package com.dm.MedicalDocumentation.patient;

import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.doctor.DoctorRepository;
import com.dm.MedicalDocumentation.healthInsurance.HealthInsurance;
import com.dm.MedicalDocumentation.healthInsurance.HealthInsuranceRepository;
import com.dm.MedicalDocumentation.patient.insuranceHistory.PatientInsuranceHistoryService;
import com.dm.MedicalDocumentation.person.Person;
import com.dm.MedicalDocumentation.person.PersonRepository;
import com.dm.MedicalDocumentation.user.User;
import com.dm.MedicalDocumentation.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository repository;
    private final DoctorRepository doctorRepository;
    private final HealthInsuranceRepository healthInsuranceRepository;
    private final PatientInsuranceHistoryService patientInsuranceHistoryService;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;

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

    public List<String> getAllPatients() {
        List<Patient> patients = repository.findAll();
        List<String> result = new ArrayList<>(patients.size());
        for (Patient patient : patients) {
            result.add(patient.getPerson().getBirthNumber() + " " + patient.getPerson().getFullName());
        }
        return result;
    }

    public boolean healthInsuranceEquals(PatientInsuranceChangeRequest request) {
        Optional<Patient> patient = repository.findByPersonBirthNumber(request.getPatient());
        return patient.isPresent()
                && patient.get().getHealthInsurance().getInsuranceName().equals(request.getHealthInsurance());
    }

    @Transactional
    public void changeHealthInsurance(PatientInsuranceChangeRequest request) {
        Patient patient = repository.findByPersonBirthNumber(request.getPatient())
                .orElseThrow(() -> new IllegalArgumentException("No patient with given birthNumber found!"));
        HealthInsurance healthInsurance = healthInsuranceRepository.findByInsuranceName(request.getHealthInsurance())
                .orElseThrow(() -> new IllegalArgumentException("No healthInsurance with given name found!"));

        patient.setHealthInsurance(healthInsurance);
        repository.save(patient);

        patientInsuranceHistoryService.createHealthInsuranceHistory(patient, false);
    }

    public boolean recordExists(PatientRequest request) {
        Optional<Patient> patient = repository.findByPersonBirthNumber(request.getPerson());
        Optional<User> user = userRepository.findByUserLogin(request.getUserLogin());
        Patient foundPatient = null;
        if (user.isPresent()) {
            foundPatient = user.get().getPatient();
        }
        return patient.isPresent() || foundPatient != null;
    }

    @Transactional
    public void createPatient(PatientRequest request) {
        Person person = personRepository.findByBirthNumber(request.getPerson())
                .orElseThrow(() -> new IllegalArgumentException("No person with birthNumber " + request.getPerson() + " exists."));
        Doctor generalPractitioner = doctorRepository.findByPersonBirthNumber(request.getGeneralPractitioner())
                .orElseThrow(() -> new IllegalArgumentException("No doctor with birthNumber " + request.getGeneralPractitioner() + " exists."));
        HealthInsurance healthInsurance = healthInsuranceRepository.findByInsuranceName(request.getHealthInsurance())
                .orElseThrow(() -> new IllegalArgumentException("No health insurance with name " + request.getHealthInsurance() + " exists."));
        User user = userRepository.findByUserLogin(request.getUserLogin())
                .orElseThrow(() -> new IllegalArgumentException("No user with login " + request.getUserLogin() + " exists."));

        Patient patient = Patient.builder()
                .user(user)
                .person(person)
                .generalPractitioner(generalPractitioner)
                .healthInsurance(healthInsurance)
                .build();
        repository.save(patient);

        patientInsuranceHistoryService.createHealthInsuranceHistory(patient, true);

    }
}
