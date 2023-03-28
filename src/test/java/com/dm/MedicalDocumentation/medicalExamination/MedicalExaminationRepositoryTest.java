package com.dm.MedicalDocumentation.medicalExamination;

import com.dm.MedicalDocumentation.city.City;
import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.healthInsurance.HealthInsurance;
import com.dm.MedicalDocumentation.hospital.Hospital;
import com.dm.MedicalDocumentation.hospital.department.Department;
import com.dm.MedicalDocumentation.hospital.department.DepartmentID;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentType;
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

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
class MedicalExaminationRepositoryTest {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private MedicalExaminationRepository underTest;

    @Test
    void findExamsWithinDepartmentAndWithAccessTest() {
        ExaminationType examinationType = ExaminationType.builder().examinationTypeName("Examination").build();
        entityManager.persist(examinationType);
        DepartmentType departmentType1 = DepartmentType.builder().departmentTypeName("Department1").build();
        entityManager.persist(departmentType1);
        DepartmentType departmentType2 = DepartmentType.builder().departmentTypeName("Department2").build();
        entityManager.persist(departmentType2);
        Hospital hospital = Hospital.builder().hospitalName("Hospital").build();
        entityManager.persist(hospital);
        Department department = Department.builder().id(DepartmentID.builder().hospital(hospital).departmentType(departmentType2).build())
                .build();
        entityManager.persist(department);
        Department department2 = Department.builder().id(DepartmentID.builder().hospital(hospital).departmentType(departmentType1).build())
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
        MedicalExamination medExam1 = MedicalExamination.builder().type(examinationType).departmentType(departmentType2).doctor(doctor)
                .patient(patient).disease(null).startTime(LocalDateTime.now()).endTime(LocalDateTime.now().plusMinutes(30)).build();
        entityManager.persist(medExam1);
        MedicalExamination medExam2 = MedicalExamination.builder().type(examinationType).departmentType(departmentType2).doctor(doctor)
                .patient(patient).disease(null).startTime(LocalDateTime.now().plusMinutes(1)).endTime(LocalDateTime.now().plusMinutes(30)).build();
        entityManager.persist(medExam2);

        Person patientPerson2 = Person.builder().name("Patient2").surname("Patient2").birthDate(LocalDate.of(1981, 2,18))
                .birthNumber("810218/8318").city(city).address("Address 1").build();
        entityManager.persist(patientPerson2);
        Patient patient2 = Patient.builder().user(patientUser).generalPractitioner(generalPractitioner)
                .person(patientPerson2).healthInsurance(healthInsurance).build();
        entityManager.persist(patient2);

        MedicalExamination medExam3 = MedicalExamination.builder().type(examinationType).departmentType(departmentType2).doctor(doctor)
                .patient(patient2).disease(null).startTime(LocalDateTime.now().plusMinutes(2)).endTime(LocalDateTime.now().plusMinutes(30)).build();
        entityManager.persist(medExam3);
        MedicalExamination medExam4 = MedicalExamination.builder().type(examinationType).departmentType(departmentType2).doctor(doctor)
                .patient(patient2).disease(null).startTime(LocalDateTime.now().plusMinutes(3)).endTime(LocalDateTime.now().plusMinutes(30)).build();
        entityManager.persist(medExam4);

        Person doctorPerson2 = Person.builder().name("Doctor2").surname("Doctor2").birthDate(LocalDate.of(1981, 2,18))
                .birthNumber("810218/8319").city(city).address("Address 1").build();
        entityManager.persist(doctorPerson2);
        Doctor doctor2 = Doctor.builder().person(doctorPerson2).department(department2).user(GPUser).build();
        entityManager.persist(doctor2);

        MedicalExamination medExam5 = MedicalExamination.builder().type(examinationType).departmentType(departmentType1).doctor(doctor2)
                .patient(patient).disease(null).startTime(LocalDateTime.now().plusMinutes(4)).endTime(LocalDateTime.now().plusMinutes(30)).build();
        entityManager.persist(medExam5);
        MedicalExamination medExam6 = MedicalExamination.builder().type(examinationType).departmentType(departmentType1).doctor(doctor2)
                .patient(patient).disease(null).startTime(LocalDateTime.now().plusMinutes(5)).endTime(LocalDateTime.now().plusMinutes(30)).build();
        entityManager.persist(medExam6);

        Person doctorPerson3 = Person.builder().name("Doctor3").surname("Doctor3").birthDate(LocalDate.of(1981, 2,18))
                .birthNumber("810218/8320").city(city).address("Address 1").build();
        entityManager.persist(doctorPerson3);
        Doctor doctor3 = Doctor.builder().person(doctorPerson2).department(department2).user(GPUser).build();
        entityManager.persist(doctor3);

        MedicalExamination medExam7 = MedicalExamination.builder().type(examinationType).departmentType(departmentType1).doctor(doctor3)
                .patient(patient).disease(null).startTime(LocalDateTime.now().plusMinutes(6)).endTime(LocalDateTime.now().plusMinutes(30)).build();
        entityManager.persist(medExam7);
        MedicalExamination medExam8 = MedicalExamination.builder().type(examinationType).departmentType(departmentType1).doctor(doctor3)
                .patient(patient).disease(null).startTime(LocalDateTime.now().plusMinutes(7)).endTime(LocalDateTime.now().plusMinutes(30)).build();
        entityManager.persist(medExam8);

        Pageable page = PageRequest.of(0, 10);
        Page<MedicalExamination> GPPatientExaminationsPage = underTest.findPatientsExamsWithinDepartmentAndWithAccess(generalPractitioner,
                generalPractitioner.getDepartment().getId().getDepartmentType(), patient, page);
        assertArrayEquals(new Object[]{medExam8, medExam7, medExam6, medExam5, medExam2, medExam1}, GPPatientExaminationsPage.getContent().toArray());

        Page<MedicalExamination> GPAllExaminationsPage = underTest.findAllExamsWithinDepartmentAndWithAccess(generalPractitioner,
                generalPractitioner.getDepartment().getId().getDepartmentType(), List.of(patient, patient2), page);
        assertArrayEquals(new Object[]{medExam8, medExam7, medExam6, medExam5, medExam4, medExam3, medExam2, medExam1}, GPAllExaminationsPage.getContent().toArray());

        Page<MedicalExamination> doctor3PatientsExaminationsPage = underTest.findPatientsExamsWithinDepartmentAndWithAccess(
                doctor3, doctor3.getDepartment().getId().getDepartmentType(), patient, page);
        assertArrayEquals(new Object[]{medExam8, medExam7, medExam6, medExam5}, doctor3PatientsExaminationsPage.getContent().toArray());
    }
}