package com.dm.MedicalDocumentation.prescription;

import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.doctor.DoctorRepository;
import com.dm.MedicalDocumentation.medication.Medication;
import com.dm.MedicalDocumentation.medication.MedicationRepository;
import com.dm.MedicalDocumentation.patient.Patient;
import com.dm.MedicalDocumentation.patient.PatientRepository;
import com.dm.MedicalDocumentation.response.CountsByMonthResponse;
import com.dm.MedicalDocumentation.response.PrescriptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

    public CountsByMonthResponse getPrescriptionCountsForLastYear(String userLogin, boolean isDoctor) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().withDayOfMonth(now.getMonth().maxLength()).with(LocalTime.MAX);
        LocalDateTime startDate = endDate.minusMonths(11).withDayOfMonth(1).with(LocalTime.MIN);
        List<Object[]> data;
        if (isDoctor) {
            Doctor doctor = doctorRepository.findByUserUserLogin(userLogin)
                    .orElseThrow(() -> new UsernameNotFoundException("No doctor with given login found!"));
            data = repository.getDoctorPrescriptionCountByMonth(doctor, startDate, endDate);
        } else {
            Patient patient = patientRepository.findByUserUserLogin(userLogin)
                    .orElseThrow(() -> new UsernameNotFoundException("No patient with given login found!"));
            data = repository.getPatientsPrescriptionCountByMonth(patient, startDate, endDate);
        }
        int startMonth = startDate.getMonthValue();
        List<Long> counts = new ArrayList<>(12);
        List<String> months = new ArrayList<>(12);
        for (int i = startMonth; i < startMonth + 12; i++) {
            int month = ((i - 1) % 12) + 1;
            String monthName = Month.of(month).getDisplayName(TextStyle.SHORT_STANDALONE, new Locale("sk"));
            months.add(monthName);

            long countInMonth = 0;
            //row[0] is count, row[1] is month
            for (Object[] row : data) {
                if ((int)row[1] == month) {
                    countInMonth = (long) row[0];
                    break;
                }
            }
            counts.add(countInMonth);
        }
        return CountsByMonthResponse.builder()
                .counts(counts)
                .months(months)
                .build();
    }
}
