package com.dm.MedicalDocumentation.prescription;

import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.doctor.DoctorRepository;
import com.dm.MedicalDocumentation.medication.Medication;
import com.dm.MedicalDocumentation.medication.MedicationRepository;
import com.dm.MedicalDocumentation.patient.Patient;
import com.dm.MedicalDocumentation.patient.PatientRepository;
import com.dm.MedicalDocumentation.response.CountsByMonthResponse;
import com.dm.MedicalDocumentation.response.PrescriptionResponse;
import com.dm.MedicalDocumentation.util.GraphDataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository repository;
    private final PatientRepository patientRepository;
    private final MedicationRepository medicationRepository;
    private final DoctorRepository doctorRepository;

    public List<PrescriptionResponse> getPatientsPrescriptions(String userLogin, String medication) {
        List<Prescription> prescriptions = repository
                .findByPatientUserUserLoginAndMedicationMedicationNameOrderByRetrievedAtAsc(userLogin, medication);
        List<PrescriptionResponse> result = new ArrayList<>(prescriptions.size());

        for (Prescription prescription : prescriptions) {
            LocalDateTime retrievedAt = prescription.getRetrievedAt() == null
                    ? LocalDateTime.of(0, 1, 1, 0, 0)
                    : prescription.getRetrievedAt();
            result.add(PrescriptionResponse.builder()
                            .medicationName(prescription.getMedication().getMedicationName())
                            .medicationAmount(prescription.getMedication().getAmount() + " " + prescription.getMedication().getUnit())
                            .packageCount(prescription.getAmount())
                            .doctor(prescription.getDoctor().getPerson().getFullName())
                            .doctorId(prescription.getDoctor().getDoctorId())
                            .prescribedAt(prescription.getPrescribedAt())
                            .retrievedAt(retrievedAt)
                    .build());
        }

        return result;
    }

    public List<String> getPatientsMedicationsByUserLogin(String userLogin) {
        Patient patient = patientRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new UsernameNotFoundException("No patient with userLogin" + userLogin + "exists."));
        return repository.findPatientsMedications(patient);
    }

    public List<String> getPatientsMedicationsByBirthNumber(String birthNumber) {
        Optional<Patient> patient = patientRepository.findByPersonBirthNumber(birthNumber);
        return patient.map(repository::findPatientsMedications).orElse(new ArrayList<>());
    }

    public void createPrescription(String userLogin, PrescriptionRequest request) {
        Patient patient = patientRepository.findByPersonBirthNumber(request.getPatient())
                .orElseThrow(() -> new UsernameNotFoundException("No patient with given birthNumber exists."));
        Medication medication = medicationRepository.findByMedicationName(request.getMedication())
                .orElseThrow(() -> new UsernameNotFoundException("No medication with given name exists."));
        Doctor doctor = doctorRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new IllegalArgumentException("No doctor with given userLogin found!"));
        Prescription prescription = Prescription.builder()
                .medication(medication)
                .patient(patient)
                .doctor(doctor)
                .amount(request.getAmount())
                .prescribedAt(LocalDateTime.now())
                .retrievedAt(null)
                .build();
        repository.save(prescription);
    }

    public Long getDoctorsTotalPrescriptionCount(String userLogin, boolean isDoctor) {
        long count;
        if (isDoctor) {
            Doctor doctor = doctorRepository.findByUserUserLogin(userLogin)
                    .orElseThrow(() -> new UsernameNotFoundException("No doctor with given login found!"));
            count = repository.countByDoctor(doctor);
        } else {
            Patient patient = patientRepository.findByUserUserLogin(userLogin)
                    .orElseThrow(() -> new UsernameNotFoundException("No patient with given login found!"));
            count = repository.countByPatient(patient);
        }
        return count;
    }

    public Long getPatientsPrescriptionCountToRetrieve(String userLogin) {
        Patient patient = patientRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new UsernameNotFoundException("No patient with given login found!"));
        LocalDateTime dateTimeSince = LocalDateTime.now().minusDays(7);
        return repository.countByPatientAndPrescribedAtGreaterThanEqualAndRetrievedAtIsNull(patient, dateTimeSince);
    }

    public CountsByMonthResponse getPrescriptionCountsForLastYear(String userLogin, LocalDate dateSince, LocalDate dateUntil, String interval, boolean isDoctor) {
        boolean monthInterval = interval.equals("month");
        LocalDateTime endDate;
        LocalDateTime startDate;
        if (monthInterval) {
            startDate = dateSince.atStartOfDay().withDayOfMonth(1);
            if (dateUntil.getMonth() == Month.FEBRUARY && !Year.isLeap(dateUntil.getYear())) {
                endDate = dateUntil.atStartOfDay().withDayOfMonth(28).with(LocalTime.MAX);
            } else {
                endDate = dateUntil.atStartOfDay().withDayOfMonth(dateUntil.getMonth().maxLength()).with(LocalTime.MAX);
            }
        } else {
            DayOfWeek startDay = dateSince.getDayOfWeek();
            startDate = dateSince.minusDays(startDay.getValue() - 1).atStartOfDay();
            DayOfWeek endDay = dateUntil.getDayOfWeek();
            endDate = dateUntil.plusDays(7 - endDay.getValue()).atTime(LocalTime.MAX);
        }

        List<Object[]> data;
        if (isDoctor) {
            Doctor doctor = doctorRepository.findByUserUserLogin(userLogin)
                    .orElseThrow(() -> new UsernameNotFoundException("No doctor with given login found!"));
            data = monthInterval
                    ? repository.getDoctorPrescriptionCountByMonth(doctor, startDate, endDate)
                    : repository.getDoctorPrescriptionCountByWeek(doctor, startDate, endDate);
        } else {
            Patient patient = patientRepository.findByUserUserLogin(userLogin)
                    .orElseThrow(() -> new UsernameNotFoundException("No patient with given login found!"));
            data = monthInterval
                    ? repository.getPatientsPrescriptionCountByMonth(patient, startDate, endDate)
                    : repository.getPatientsPrescriptionCountByWeek(patient, startDate, endDate);
        }
        return GraphDataUtil.getCountsByInterval(startDate, endDate, data, monthInterval);
    }
}
