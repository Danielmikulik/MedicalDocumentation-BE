package com.dm.MedicalDocumentation.prescription;

import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.doctor.DoctorRepository;
import com.dm.MedicalDocumentation.medication.Medication;
import com.dm.MedicalDocumentation.medication.MedicationRepository;
import com.dm.MedicalDocumentation.patient.Patient;
import com.dm.MedicalDocumentation.patient.PatientRepository;
import com.dm.MedicalDocumentation.response.CountsByMonthResponse;
import com.dm.MedicalDocumentation.response.CustomPage;
import com.dm.MedicalDocumentation.response.PrescriptionResponse;
import com.dm.MedicalDocumentation.util.GraphDataUtil;
import com.dm.MedicalDocumentation.util.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository repository;
    private final PatientRepository patientRepository;
    private final MedicationRepository medicationRepository;
    private final DoctorRepository doctorRepository;

    public CustomPage<PrescriptionResponse> getPatientsPrescriptions(String userLogin, String medication, Pageable page) {
        Page<Prescription> prescriptions = medication.isBlank()
                ? repository.findByPatientUserUserLoginOrderByRetrievedAtAsc(userLogin, page)
                : repository.findByPatientUserUserLoginAndMedicationMedicationNameOrderByRetrievedAtAsc(userLogin, medication, page);
        List<PrescriptionResponse> result = new ArrayList<>(prescriptions.getContent().size());

        for (Prescription prescription : prescriptions.getContent()) {
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

        return new CustomPage<>(result, prescriptions.getTotalElements(), prescriptions.getTotalPages());
    }

    public List<String> getPatientsMedicationsByUserLogin(String userLogin) {
        Patient patient = patientRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new UsernameNotFoundException("No patient with userLogin" + userLogin + "exists."));
        return repository.findPatientsMedications(patient);
    }

    public List<String> getPatientsMedicationsFullByUserLogin(String userLogin) {
        Patient patient = patientRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new UsernameNotFoundException("No patient with userLogin" + userLogin + "exists."));
        List<Medication> medications = repository.findPatientsMedicationsFull(patient);
        return ResultUtil.getMedicationsAsStringList(medications);
    }

    public List<String> getPatientsMedicationsByBirthNumber(String birthNumber) {
        Optional<Patient> patient = patientRepository.findByPersonBirthNumber(birthNumber);
        return patient.map(repository::findPatientsMedications).orElse(new ArrayList<>());
    }

    public void createPrescription(String userLogin, PrescriptionRequest request) {
        Patient patient = patientRepository.findByPersonBirthNumber(request.getPatient())
                .orElseThrow(() -> new IllegalArgumentException("No patient with given birthNumber exists."));
        Medication medication = medicationRepository.findByMedicationName(request.getMedication())
                .orElseThrow(() -> new IllegalArgumentException("No medication with given name exists."));
        Doctor doctor = doctorRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new UsernameNotFoundException("No doctor with given userLogin found!"));
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

    public Long getDoctorsTotalPrescriptionCount(String userLogin, String medication) {
        Doctor doctor = doctorRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new UsernameNotFoundException("No doctor with given login found!"));
        if (!medication.isBlank()) {
            Medication foundMedication = findMedicationByNameAndAmount(medication);
            return repository.countByDoctorAndMedication(doctor, foundMedication);
        }
        return repository.countByDoctor(doctor);
    }

    public Long getPatientsTotalPrescriptionCount(String userLogin, String medication) {
        Patient patient = patientRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new UsernameNotFoundException("No patient with given login found!"));
        if (!medication.isBlank()) {
            Medication foundMedication = findMedicationByNameAndAmount(medication);
            return repository.countByPatientAndMedication(patient, foundMedication);
        }
        return repository.countByPatient(patient);
    }

    public Long getPatientsPrescriptionCountToRetrieve(String userLogin, String medication) {
        Patient patient = patientRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new UsernameNotFoundException("No patient with given login found!"));
        LocalDateTime dateTimeSince = LocalDateTime.now().minusDays(7);
        if (!medication.isBlank()) {
            Medication foundMedication = findMedicationByNameAndAmount(medication);
            return repository.countByPatientAndPrescribedAtGreaterThanEqualAndRetrievedAtIsNullAndMedication(
                    patient, dateTimeSince, foundMedication);
        }
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

    private Medication findMedicationByNameAndAmount(String medication) {
        String[] medicationArray = medication.split(" ");
        String medicationName = String.join(" ", Arrays.copyOf(medicationArray, medicationArray.length - 2));
        int amount = Integer.parseInt(medicationArray[medicationArray.length - 2]);
        return medicationRepository.findByMedicationNameAndAmount(medicationName, amount)
                .orElseThrow(() -> new IllegalArgumentException("No medication with given name exists."));
    }

    public CustomPage<PrescriptionResponse> getNonRetrievedPrescriptions(Pageable page) {
        LocalDateTime lastWeek = LocalDateTime.now().minusDays(7);
        Page<Prescription> prescriptions = repository.findByRetrievedAtIsNullAndPrescribedAtGreaterThanEqual(page, lastWeek);
        List<PrescriptionResponse> result = new ArrayList<>(prescriptions.getContent().size());

        for (Prescription prescription : prescriptions) {
            result.add(PrescriptionResponse.builder()
                    .prescriptionId(prescription.getPrescriptionId())
                    .medicationName(prescription.getMedication().getMedicationName())
                    .medicationAmount(prescription.getMedication().getAmount() + " " + prescription.getMedication().getUnit())
                    .packageCount(prescription.getAmount())
                    .doctor(prescription.getDoctor().getPerson().getFullName())
                    .doctorId(prescription.getDoctor().getDoctorId())
                    .prescribedAt(prescription.getPrescribedAt())
                    .build());
        }
        return new CustomPage<>(result, prescriptions.getTotalElements(), prescriptions.getTotalPages());
    }

    public String confirmPrescription(List<Long> ids) {
        int updateCount = 0;
        List<Prescription> requests = new ArrayList<>(ids.size());
        for (long id : ids) {
            Optional<Prescription> prescription = repository.findById(id);
            if (prescription.isPresent() && prescription.get().getRetrievedAt() == null) {
                prescription.get().setRetrievedAt(LocalDateTime.now());
                requests.add(prescription.get());
                updateCount++;
            }
        }
        repository.saveAll(requests);
        return String.valueOf(updateCount);
    }
}
