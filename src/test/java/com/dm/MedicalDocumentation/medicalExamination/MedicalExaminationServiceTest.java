package com.dm.MedicalDocumentation.medicalExamination;

import com.dm.MedicalDocumentation.accessRequest.AccessRequest;
import com.dm.MedicalDocumentation.accessRequest.AccessRequestRepository;
import com.dm.MedicalDocumentation.attachment.AttachmentRepository;
import com.dm.MedicalDocumentation.disease.Disease;
import com.dm.MedicalDocumentation.disease.DiseaseRepository;
import com.dm.MedicalDocumentation.disease.type.DiseaseType;
import com.dm.MedicalDocumentation.disease.type.DiseaseTypeRepository;
import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.doctor.DoctorRepository;
import com.dm.MedicalDocumentation.healthInsurance.HealthInsurance;
import com.dm.MedicalDocumentation.hospital.Hospital;
import com.dm.MedicalDocumentation.hospital.department.Department;
import com.dm.MedicalDocumentation.hospital.department.DepartmentID;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentType;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentTypeRepository;
import com.dm.MedicalDocumentation.medicalExamination.type.ExaminationType;
import com.dm.MedicalDocumentation.medicalExamination.type.ExaminationTypeRepository;
import com.dm.MedicalDocumentation.patient.Patient;
import com.dm.MedicalDocumentation.patient.PatientRepository;
import com.dm.MedicalDocumentation.person.Person;
import com.dm.MedicalDocumentation.request.MedicalExamRequest;
import com.dm.MedicalDocumentation.user.Role;
import com.dm.MedicalDocumentation.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MedicalExaminationServiceTest {

    @Mock private MedicalExaminationRepository repository;
    @Mock private DoctorRepository doctorRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private AccessRequestRepository accessRequestRepository;
    @Mock private DiseaseRepository diseaseRepository;
    @Mock private DiseaseTypeRepository diseaseTypeRepository;
    @Mock private ExaminationTypeRepository examinationTypeRepository;
    @Mock private DepartmentTypeRepository departmentTypeRepository;
    @Mock private AttachmentRepository attachmentRepository;
    private MedicalExaminationService underTest;

    @BeforeEach
    void setUp() {
        underTest = new MedicalExaminationService(repository, doctorRepository, patientRepository, accessRequestRepository,
                diseaseRepository, diseaseTypeRepository, examinationTypeRepository, attachmentRepository, departmentTypeRepository);
    }

    @Test
    void doctorNotExists() {
        String userLogin = "nonExistingLogin";
        assertThatThrownBy(() -> underTest.getDoctorsExams(userLogin, null, false, null))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("No doctor with given login found.");
        verify(doctorRepository, times(1)).findByUserUserLogin(userLogin);
    }

    @Test
    void patientNotExists() {
        String doctorLogin = "doctor";
        Doctor doctor = new Doctor(1L, new Person(), new User(), new Department(), null);
        given(doctorRepository.findByUserUserLogin(doctorLogin)).willReturn(Optional.of(doctor));
        assertThatThrownBy(() -> underTest.getDoctorsExams(doctorLogin, "birthNumber", false, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No patient with given birth number found.");
        verify(doctorRepository, times(1)).findByUserUserLogin(doctorLogin);
        verify(patientRepository, times(1)).findByPersonBirthNumber("birthNumber");
    }

    @Test
    void patientsExamsAreFoundForGP() {
        String doctorLogin = "doctor";
        Department department = new Department(new DepartmentID(new DepartmentType(), new Hospital()));
        Doctor doctor = new Doctor(1L, new Person(), new User(), department, null);
        String patientBirthNumber = "birthNumber";
        Patient patient = new Patient(1L, new Person(), doctor, new HealthInsurance(), new User());

        given(doctorRepository.findByUserUserLogin(doctorLogin)).willReturn(Optional.of(doctor));
        given(patientRepository.findByPersonBirthNumber(patientBirthNumber)).willReturn(Optional.of(patient));
        given(repository.findPatientsExamsWithinDepartmentAndWithAccess(eq(doctor), any(), any(), any())).willReturn(Page.empty());

        underTest.getDoctorsExams(doctorLogin, patientBirthNumber, true, null);

        verify(doctorRepository, times(1)).findByUserUserLogin(doctorLogin);
        verify(patientRepository, times(1)).findByPersonBirthNumber(patientBirthNumber);
        verify(patientRepository, times(1)).findGeneralPractitionersPatients(doctor);
        verify(patientRepository, times(0)).findDoctorsPatients(any());

        verify(repository, times(0)).findAllExamsWithinDepartmentAndWithAccess(eq(doctor), any(), any(), any());
        verify(repository, times(1)).findPatientsExamsWithinDepartmentAndWithAccess(eq(doctor), any(), any(), any());
    }

    @Test
    void allExamsAreFoundForNonGP() {
        String doctorLogin = "doctor";
        Department department = new Department(new DepartmentID(new DepartmentType(), new Hospital()));
        Doctor doctor = new Doctor(1L, new Person(), new User(), department, null);

        given(doctorRepository.findByUserUserLogin(doctorLogin)).willReturn(Optional.of(doctor));
        given(repository.findAllExamsWithinDepartmentAndWithAccess(eq(doctor), any(), any(), any())).willReturn(Page.empty());

        underTest.getDoctorsExams(doctorLogin, null, false, null);

        verify(doctorRepository, times(1)).findByUserUserLogin(doctorLogin);
        verify(patientRepository, times(0)).findByPersonBirthNumber(anyString());
        verify(patientRepository, times(0)).findGeneralPractitionersPatients(any());
        verify(patientRepository, times(1)).findDoctorsPatients(doctor.getDoctorId());

        verify(repository, times(1)).findAllExamsWithinDepartmentAndWithAccess(eq(doctor), any(), any(), any());
        verify(repository, times(0)).findPatientsExamsWithinDepartmentAndWithAccess(eq(doctor), any(), any(), any());
    }

    @Test
    void hasUserAccessSameDoctor() {
        String doctorLogin = "doctor";
        Department department = new Department(new DepartmentID(new DepartmentType(), new Hospital()));
        Doctor doctor = new Doctor(1L, new Person(), new User(), department, null);
        MedicalExamination exam = new MedicalExamination(1L, new ExaminationType(), new Disease(), new Patient(), doctor, new DepartmentType(),
                LocalDateTime.now(), LocalDateTime.now(), null);

        given(repository.findById(1L)).willReturn(Optional.of(exam));
        given(doctorRepository.findByUserUserLogin(doctorLogin)).willReturn(Optional.of(doctor));

        boolean hasAccess = underTest.hasUserAccess(1L, doctorLogin, Role.DOCTOR);
        assertTrue(hasAccess);

        verify(repository, times(1)).findById(1L);
        verify(doctorRepository, times(1)).findByUserUserLogin(doctorLogin);
        verify(accessRequestRepository, times(0)).findByMedicalExaminationMedicalExaminationId(any());
    }

    @Test
    void hasUserAccessTroughRequest() {
        String doctorLogin = "doctor";
        Department department = new Department(new DepartmentID(new DepartmentType(), new Hospital()));
        Doctor doctor = new Doctor(1L, new Person(), new User(), department, null);
        Doctor generalPractitioner = new Doctor(2L, new Person(), new User(), new Department(), null);
        MedicalExamination exam = new MedicalExamination(1L, new ExaminationType(), new Disease(),
                Patient.builder().generalPractitioner(generalPractitioner).build(),
                new Doctor(), null, LocalDateTime.now(), LocalDateTime.now(), null);
        AccessRequest request = new AccessRequest(1, exam, doctor, true, false, LocalDate.now().plusYears(1));
        given(repository.findById(1L)).willReturn(Optional.of(exam));
        given(doctorRepository.findByUserUserLogin(doctorLogin)).willReturn(Optional.of(doctor));
        given(accessRequestRepository.findByMedicalExaminationMedicalExaminationId(exam.getMedicalExaminationId()))
                .willReturn(List.of(request));

        boolean hasAccess = underTest.hasUserAccess(1L, doctorLogin, Role.DOCTOR);
        assertTrue(hasAccess);

        verify(repository, times(1)).findById(1L);
        verify(doctorRepository, times(1)).findByUserUserLogin(doctorLogin);
        verify(accessRequestRepository, times(1))
                .findByMedicalExaminationMedicalExaminationId(exam.getMedicalExaminationId());
    }

    @Test
    void userDoesNotHaveAccess() {
        String doctorLogin = "doctor";
        Department department = new Department(new DepartmentID(new DepartmentType(), new Hospital()));
        Doctor doctor = new Doctor(1L, new Person(), new User(), department, null);
        Doctor generalPractitioner = new Doctor(2L, new Person(), new User(), new Department(), null);
        MedicalExamination exam = new MedicalExamination(1L, new ExaminationType(), new Disease(),
                Patient.builder().generalPractitioner(generalPractitioner).build(),
                new Doctor(), null, LocalDateTime.now(), LocalDateTime.now(), null);
        AccessRequest request = new AccessRequest(1, exam, doctor, true, false, LocalDate.now().minusYears(1));
        given(repository.findById(1L)).willReturn(Optional.of(exam));
        given(doctorRepository.findByUserUserLogin(doctorLogin)).willReturn(Optional.of(doctor));
        given(accessRequestRepository.findByMedicalExaminationMedicalExaminationId(exam.getMedicalExaminationId()))
                .willReturn(List.of(request));

        boolean hasAccess = underTest.hasUserAccess(1L, doctorLogin, Role.DOCTOR);
        assertFalse(hasAccess);

        verify(repository, times(1)).findById(1L);
        verify(doctorRepository, times(1)).findByUserUserLogin(doctorLogin);
        verify(accessRequestRepository, times(1))
                .findByMedicalExaminationMedicalExaminationId(exam.getMedicalExaminationId());
    }

    @Test
    void createMedicalExam() {
        String doctorLogin = "doctor";
        Department department = new Department(new DepartmentID(new DepartmentType(), new Hospital()));
        Doctor doctor = new Doctor(1L, new Person(), new User(), department, null);
        Patient patient = new Patient(1L, new Person(), doctor, new HealthInsurance(), new User());

        MedicalExamRequest request = new MedicalExamRequest();
        request.setPatient("birthNumber");
        request.setDiseaseType("diseaseType");
        request.setStartTime(LocalDateTime.now());
        request.setEndTime(LocalDateTime.now());

        given(doctorRepository.findByUserUserLogin(doctorLogin)).willReturn(Optional.of(doctor));
        given(patientRepository.findByPersonBirthNumber(anyString())).willReturn(Optional.of(patient));
        given(diseaseTypeRepository.findByDiseaseTypeName(anyString())).willReturn(Optional.of(new DiseaseType()));
        given(examinationTypeRepository.findByExaminationTypeName(any())).willReturn(Optional.of(new ExaminationType()));

        underTest.createMedicalExam(doctorLogin, request);

        verify(doctorRepository, times(1)).findByUserUserLogin(doctorLogin);
        verify(patientRepository, times(1)).findByPersonBirthNumber(anyString());
        verify(diseaseTypeRepository, times(1)).findByDiseaseTypeName(any());
        verify(diseaseRepository, times(1)).findByPatientAndDiseaseTypeAndCured(any(), any(), any());
        verify(diseaseRepository, times(1)).save(any());
        verify(attachmentRepository, times(0)).save(any());
    }

    @Test
    void getTotalExamCountDoctor() {
        String userLogin = "login";
        Doctor doctor = new Doctor(1L, new Person(), new User(), new Department(), null);
        DiseaseType diseaseType = new DiseaseType(1, "disease");

        given(doctorRepository.findByUserUserLogin(userLogin)).willReturn(Optional.of(doctor));
        given(diseaseTypeRepository.findByDiseaseTypeName(diseaseType.getDiseaseTypeName())).willReturn(Optional.of(diseaseType));

        underTest.getDoctorsTotalExamCount(userLogin, diseaseType.getDiseaseTypeName());

        verify(doctorRepository, times(1)).findByUserUserLogin(userLogin);
        verify(diseaseTypeRepository, times(1)).findByDiseaseTypeName(diseaseType.getDiseaseTypeName());
        verify(repository, times(1)).countByDoctorAndDiseaseDiseaseType(doctor, diseaseType);
    }

    @Test
    void getTotalExamCountPatient() {
        String userLogin = "login";
        Patient patient = new Patient(1L, new Person(), new Doctor(), new HealthInsurance(), new User());

        given(patientRepository.findByUserUserLogin(userLogin)).willReturn(Optional.of(patient));

        underTest.getPatientsTotalExamCount(userLogin, "");

        verify(patientRepository, times(1)).findByUserUserLogin(userLogin);
        verify(departmentTypeRepository, times(0)).findByDepartmentTypeName(any());
        verify(repository, times(1)).countByPatient(patient);

    }
}