package com.dm.MedicalDocumentation.accessRequest;

import com.dm.MedicalDocumentation.city.City;
import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.healthInsurance.HealthInsurance;
import com.dm.MedicalDocumentation.hospital.Hospital;
import com.dm.MedicalDocumentation.hospital.department.Department;
import com.dm.MedicalDocumentation.hospital.department.DepartmentID;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentType;
import com.dm.MedicalDocumentation.medicalExamination.MedicalExamination;
import com.dm.MedicalDocumentation.medicalExamination.type.ExaminationType;
import com.dm.MedicalDocumentation.patient.Patient;
import com.dm.MedicalDocumentation.person.Person;
import com.dm.MedicalDocumentation.user.Role;
import com.dm.MedicalDocumentation.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AccessRequestRepositoryTest {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private AccessRequestRepository underTest;

    @Test
    void getGeneralPractitionersPatientsAccessRequestsTest() {
        ExaminationType examinationType = ExaminationType.builder().examinationTypeName("Examination").build();
        entityManager.persist(examinationType);
        DepartmentType departmentType1 = DepartmentType.builder().departmentTypeName("Department1").build();
        entityManager.persist(departmentType1);
        DepartmentType departmentType2 = DepartmentType.builder().departmentTypeName("Department2").build();
        entityManager.persist(departmentType2);
        Hospital hospital = Hospital.builder().hospitalName("Hospital").build();
        entityManager.persist(hospital);
        Department department = Department.builder().id(DepartmentID.builder().hospital(hospital).departmentType(departmentType1).build())
                .build();
        entityManager.persist(department);
        Department department2 = Department.builder().id(DepartmentID.builder().hospital(hospital).departmentType(departmentType2).build())
                .build();
        entityManager.persist(department2);
        City city = City.builder().zipCode("010 01").cityName("City").build();
        entityManager.persist(city);
        Person doctorPerson = Person.builder().name("Doctor").surname("Doctor").birthDate(LocalDate.of(1981, 2,18))
                .birthNumber("810218/8315").city(city).address("Address 1").build();
        entityManager.persist(doctorPerson);
        User doctorUser = User.builder().userLogin("Doctor").email("a")
                .password("a").telephone("").role(Role.DOCTOR).createdAt(LocalDateTime.now()).build();
        entityManager.persist(doctorUser);
        Doctor doctor = Doctor.builder().person(doctorPerson).department(department).user(doctorUser).build();
        entityManager.persist(doctor);

        Person GPPerson = Person.builder().name("GP").surname("GP").birthDate(LocalDate.of(1981, 2,18))
                .birthNumber("810218/8316").city(city).address("Address 1").build();
        entityManager.persist(GPPerson);
        User GPUser = User.builder().userLogin("GP").email("b").password("b")
                .telephone("").role(Role.DOCTOR).createdAt(LocalDateTime.now()).build();
        entityManager.persist(GPUser);
        Doctor generalPractitioner = Doctor.builder().person(GPPerson).department(department).user(GPUser).build();
        entityManager.persist(generalPractitioner);

        User patientUser = User.builder().userLogin("Patient").email("c").password("c").telephone("").role(Role.PATIENT)
                .createdAt(LocalDateTime.now()).build();
        entityManager.persist(patientUser);
        Person patientPerson = Person.builder().name("Patient").surname("Patient").birthDate(LocalDate.of(1981, 2,18))
                .birthNumber("810218/8317").city(city).address("Address 1").build();
        entityManager.persist(patientPerson);
        HealthInsurance healthInsurance = HealthInsurance.builder().insuranceName("Insurance").build();
        entityManager.persist(healthInsurance);
        Patient patient = Patient.builder().user(patientUser).generalPractitioner(generalPractitioner)
                .person(patientPerson).healthInsurance(healthInsurance).build();
        entityManager.persist(patient);

        MedicalExamination medExam1 = MedicalExamination.builder().type(examinationType).departmentType(departmentType1).doctor(doctor)
                .patient(patient).disease(null).startTime(LocalDateTime.now()).endTime(LocalDateTime.now().plusMinutes(30)).build();
        entityManager.persist(medExam1);
        MedicalExamination medExam2 = MedicalExamination.builder().type(examinationType).departmentType(departmentType1).doctor(doctor)
                .patient(patient).disease(null).startTime(LocalDateTime.now().plusMinutes(1)).endTime(LocalDateTime.now().plusMinutes(30)).build();
        entityManager.persist(medExam2);

        Person patientPerson2 = Person.builder().name("Patient2").surname("Patient2").birthDate(LocalDate.of(1981, 2,18))
                .birthNumber("810218/8318").city(city).address("Address 1").build();
        entityManager.persist(patientPerson2);
        Patient patient2 = Patient.builder().user(patientUser).generalPractitioner(generalPractitioner)
                .person(patientPerson2).healthInsurance(healthInsurance).build();
        entityManager.persist(patient2);

        MedicalExamination medExam3 = MedicalExamination.builder().type(examinationType).departmentType(departmentType1).doctor(doctor)
                .patient(patient2).disease(null).startTime(LocalDateTime.now().plusMinutes(2)).endTime(LocalDateTime.now().plusMinutes(30)).build();
        entityManager.persist(medExam3);
        MedicalExamination medExam4 = MedicalExamination.builder().type(examinationType).departmentType(departmentType1).doctor(doctor)
                .patient(patient2).disease(null).startTime(LocalDateTime.now().plusMinutes(3)).endTime(LocalDateTime.now().plusMinutes(30)).build();
        entityManager.persist(medExam4);

        Person doctorPerson2 = Person.builder().name("Doctor2").surname("Doctor2").birthDate(LocalDate.of(1981, 2,18))
                .birthNumber("810218/8319").city(city).address("Address 1").build();
        entityManager.persist(doctorPerson2);
        Doctor doctor2 = Doctor.builder().person(doctorPerson2).department(department2).user(GPUser).build();
        entityManager.persist(doctor2);

        MedicalExamination medExam5 = MedicalExamination.builder().type(examinationType).departmentType(departmentType2).doctor(doctor2)
                .patient(patient).disease(null).startTime(LocalDateTime.now().plusMinutes(4)).endTime(LocalDateTime.now().plusMinutes(30)).build();
        entityManager.persist(medExam5);
        MedicalExamination medExam6 = MedicalExamination.builder().type(examinationType).departmentType(departmentType2).doctor(doctor2)
                .patient(patient).disease(null).startTime(LocalDateTime.now().plusMinutes(5)).endTime(LocalDateTime.now().plusMinutes(30)).build();
        entityManager.persist(medExam6);

        Person doctorPerson3 = Person.builder().name("Doctor3").surname("Doctor3").birthDate(LocalDate.of(1981, 2,18))
                .birthNumber("810218/8320").city(city).address("Address 1").build();
        entityManager.persist(doctorPerson3);
        Doctor doctor3 = Doctor.builder().person(doctorPerson2).department(department2).user(GPUser).build();
        entityManager.persist(doctor3);

        MedicalExamination medExam7 = MedicalExamination.builder().type(examinationType).departmentType(departmentType2).doctor(doctor3)
                .patient(patient).disease(null).startTime(LocalDateTime.now().plusMinutes(6)).endTime(LocalDateTime.now().plusMinutes(30)).build();
        entityManager.persist(medExam7);
        MedicalExamination medExam8 = MedicalExamination.builder().type(examinationType).departmentType(departmentType2).doctor(doctor3)
                .patient(patient).disease(null).startTime(LocalDateTime.now().plusMinutes(7)).endTime(LocalDateTime.now().plusMinutes(30)).build();
        entityManager.persist(medExam8);

        AccessRequest request1 = AccessRequest.builder().medicalExamination(medExam1).doctor(doctor3)
                .approved(false).rejected(false).accessibleUntil(LocalDate.now().plusYears(5)).build();
        AccessRequest request2 = AccessRequest.builder().medicalExamination(medExam2).doctor(doctor3)
                .approved(false).rejected(true).accessibleUntil(LocalDate.now().plusYears(5)).build();
        AccessRequest request3 = AccessRequest.builder().medicalExamination(medExam7).doctor(doctor)
                .approved(false).rejected(false).accessibleUntil(LocalDate.now().plusYears(5)).build();
        AccessRequest request4 = AccessRequest.builder().medicalExamination(medExam6).doctor(doctor)
                .approved(true).rejected(false).accessibleUntil(LocalDate.now().plusYears(5)).build();
        entityManager.persist(request1);
        entityManager.persist(request2);
        entityManager.persist(request3);
        entityManager.persist(request4);

        Pageable page = PageRequest.of(0, 10);
        Page<AccessRequest> nonRejectedRequestsPage = underTest.getGeneralPractitionersPatientsNonRejectedAccessRequests(generalPractitioner,
                "", "", "", "", "", page);
        assertArrayEquals(new AccessRequest[]{request1, request3}, nonRejectedRequestsPage.getContent().toArray());

        Page<AccessRequest> allRequestsPage = underTest.getGeneralPractitionersPatientsAllAccessRequests(generalPractitioner,
                "", "", "", "", "", page);
        assertArrayEquals(new AccessRequest[]{request1, request2, request3}, allRequestsPage.getContent().toArray());

        Page<AccessRequest> allFilteredRequestsPage = underTest.getGeneralPractitionersPatientsAllAccessRequests(generalPractitioner,
                patientPerson2.getFullName(), "", "", "", "", page);
        assertArrayEquals(new AccessRequest[]{request3}, allFilteredRequestsPage.getContent().toArray());

        Page<AccessRequest> rejectedPage = underTest.getGeneralPractitionersPatientsRejectedAccessRequests(generalPractitioner,
                "", "", "", "", "", page);
        assertArrayEquals(new AccessRequest[]{request2}, rejectedPage.getContent().toArray());

        List<Object[]> groups = underTest.getNonApprovedAccessRequestCounts(doctor3);
        assertArrayEquals(groups.get(0), new Object[]{1L, patient, departmentType1, false});
        assertArrayEquals(groups.get(1), new Object[]{1L, patient, departmentType1, true});
    }
}