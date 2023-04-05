package com.dm.MedicalDocumentation.accessRequest;

import com.dm.MedicalDocumentation.disease.Disease;
import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.doctor.DoctorRepository;
import com.dm.MedicalDocumentation.healthInsurance.HealthInsurance;
import com.dm.MedicalDocumentation.hospital.Hospital;
import com.dm.MedicalDocumentation.hospital.department.Department;
import com.dm.MedicalDocumentation.hospital.department.DepartmentID;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentType;
import com.dm.MedicalDocumentation.medicalExamination.MedicalExamination;
import com.dm.MedicalDocumentation.medicalExamination.MedicalExaminationRepository;
import com.dm.MedicalDocumentation.medicalExamination.type.ExaminationType;
import com.dm.MedicalDocumentation.patient.Patient;
import com.dm.MedicalDocumentation.patient.PatientRepository;
import com.dm.MedicalDocumentation.person.Person;
import com.dm.MedicalDocumentation.request.AccessRequestRequest;
import com.dm.MedicalDocumentation.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccessRequestServiceTest {

    @Mock private DoctorRepository doctorRepository;
    @Mock private MedicalExaminationRepository medExamRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private AccessRequestRepository repository;
    private AccessRequestService underTest;
    @BeforeEach
    void setUp() {
        underTest = new AccessRequestService(doctorRepository, medExamRepository, patientRepository, repository);
    }

    @Test
    void createAccessRequestsForPatientsMedExams() {
        String doctorLogin = "doctor";
        Department department = new Department(new DepartmentID(new DepartmentType(), new Hospital()));
        Doctor doctor = new Doctor(1L, new Person(), new User(), department, null);
        Patient patient = new Patient(1L, new Person(), doctor, new HealthInsurance(), new User());
        MedicalExamination exam = new MedicalExamination(1L, new ExaminationType(), new Disease(), new Patient(), doctor, new DepartmentType(),
                LocalDateTime.now(), LocalDateTime.now(), null);
        MedicalExamination exam2 = new MedicalExamination(2L, new ExaminationType(), new Disease(), new Patient(), doctor, new DepartmentType(),
                LocalDateTime.now(), LocalDateTime.now(), null);
        MedicalExamination exam3 = new MedicalExamination(3L, new ExaminationType(), new Disease(), new Patient(), doctor, new DepartmentType(),
                LocalDateTime.now(), LocalDateTime.now(), null);
        AccessRequestRequest request = new AccessRequestRequest("", "", LocalDate.now(), LocalDate.now());

        given(doctorRepository.findByUserUserLogin(doctorLogin)).willReturn(Optional.of(doctor));
        given(patientRepository.findByPersonBirthNumber(anyString())).willReturn(Optional.of(patient));
        given(medExamRepository.findByPatientAndDepartmentTypeDepartmentTypeNameAndStartTimeGreaterThan(
                patient, request.getDepartment(), request.getDateSince().atStartOfDay())).willReturn(List.of(exam, exam2, exam3));

        underTest.createAccessRequestsForPatientsMedExams(doctorLogin, request);

        verify(doctorRepository, times(1)).findByUserUserLogin(doctorLogin);
        verify(patientRepository, times(1)).findByPersonBirthNumber(anyString());
        verify(medExamRepository, times(1)).findByPatientAndDepartmentTypeDepartmentTypeNameAndStartTimeGreaterThan(
                patient, request.getDepartment(), request.getDateSince().atStartOfDay());
        verify(repository, times(3)).findByMedicalExaminationAndDoctorAndRejectedAndAccessibleUntilGreaterThanEqual(
                        any(), any(), eq(false), any());
        verify(repository, times(1)).saveAll(any());
    }

    @Test
    void getDoctorsPatientsAccessRequests() {
    }

    @Test
    void getDoctorsPatientsWithAccessRequests() {
    }

    @Test
    void updateAccessRequests() {
    }
}