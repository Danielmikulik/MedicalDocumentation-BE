package com.dm.MedicalDocumentation.prescription;

import com.dm.MedicalDocumentation.city.City;
import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.healthInsurance.HealthInsurance;
import com.dm.MedicalDocumentation.hospital.Hospital;
import com.dm.MedicalDocumentation.hospital.department.Department;
import com.dm.MedicalDocumentation.hospital.department.DepartmentID;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentType;
import com.dm.MedicalDocumentation.medication.Medication;
import com.dm.MedicalDocumentation.patient.Patient;
import com.dm.MedicalDocumentation.person.Person;
import com.dm.MedicalDocumentation.user.Role;
import com.dm.MedicalDocumentation.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PrescriptionRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private PrescriptionRepository underTest;

    @Test
    void findPatientsMedications() {
        DepartmentType departmentTypeNeurology = DepartmentType.builder().departmentTypeName("Department").build();
        entityManager.persist(departmentTypeNeurology);
        Hospital hospital = Hospital.builder().hospitalName("Hospital").build();
        entityManager.persist(hospital);
        Department department = Department.builder().id(DepartmentID.builder().hospital(hospital).departmentType(departmentTypeNeurology).build())
                .build();
        entityManager.persist(department);
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

        Person patientPerson2 = Person.builder().name("Patient2").surname("Patient2").birthDate(LocalDate.of(1981, 2,18))
                .birthNumber("810218/8318").city(city).address("Address 1").build();
        entityManager.persist(patientPerson2);
        Patient patient2 = Patient.builder().user(patientUser).generalPractitioner(generalPractitioner)
                .person(patientPerson2).healthInsurance(healthInsurance).build();
        entityManager.persist(patient2);

        Medication medication1 = Medication.builder().medicationName("medication1").build();
        Medication medication2 = Medication.builder().medicationName("medication2").build();
        Medication medication3 = Medication.builder().medicationName("medication3").build();
        entityManager.persist(medication1);
        entityManager.persist(medication2);
        entityManager.persist(medication3);

        Prescription prescription1 = Prescription.builder().medication(medication1).patient(patient).doctor(generalPractitioner)
                .amount(1).prescribedAt(LocalDateTime.now()).build();
        Prescription prescription2 = Prescription.builder().medication(medication2).patient(patient).doctor(doctor)
                .amount(1).prescribedAt(LocalDateTime.now()).build();
        Prescription prescription3 = Prescription.builder().medication(medication3).patient(patient2).doctor(generalPractitioner)
                .amount(1).prescribedAt(LocalDateTime.now()).build();
        entityManager.persist(prescription1);
        entityManager.persist(prescription2);
        entityManager.persist(prescription3);

        List<String> medicationsPatient = underTest.findPatientsMedications(patient);
        assertArrayEquals(new String[]{medication1.getMedicationName(), medication2.getMedicationName()}, medicationsPatient.toArray());

        List<String> medicationsPatient2 = underTest.findPatientsMedications(patient2);
        assertArrayEquals(new String[]{medication3.getMedicationName()}, medicationsPatient2.toArray());
    }

    @Test
    void getPrescriptionCountByMonthTest() {
        DepartmentType departmentTypeNeurology = DepartmentType.builder().departmentTypeName("Department").build();
        entityManager.persist(departmentTypeNeurology);
        Hospital hospital = Hospital.builder().hospitalName("Hospital").build();
        entityManager.persist(hospital);
        Department department = Department.builder().id(DepartmentID.builder().hospital(hospital).departmentType(departmentTypeNeurology).build())
                .build();
        entityManager.persist(department);
        City city = City.builder().zipCode("010 01").cityName("City").build();
        entityManager.persist(city);

        Person GPPerson = Person.builder().name("GP").surname("GP").birthDate(LocalDate.of(1981, 2,18))
                .birthNumber("810218/8316").city(city).address("Address 1").build();
        entityManager.persist(GPPerson);
        User GPUser = User.builder().userLogin("GP").email("b").password("b")
                .telephone("").role(Role.DOCTOR).createdAt(LocalDateTime.now()).build();
        entityManager.persist(GPUser);
        Doctor generalPractitioner = Doctor.builder().person(GPPerson).department(department).user(GPUser).build();
        entityManager.persist(generalPractitioner);

        User patientUser = User.builder().userLogin("Pacient").email("c").password("c").telephone("").role(Role.PATIENT)
                .createdAt(LocalDateTime.now()).build();
        entityManager.persist(patientUser);
        Person patientPerson = Person.builder().name("Pacient").surname("Pacient").birthDate(LocalDate.of(1981, 2,18))
                .birthNumber("810218/8317").city(city).address("Address 1").build();
        entityManager.persist(patientPerson);
        HealthInsurance healthInsurance = HealthInsurance.builder().insuranceName("VšZP").build();
        entityManager.persist(healthInsurance);
        Patient patient = Patient.builder().user(patientUser).generalPractitioner(generalPractitioner)
                .person(patientPerson).healthInsurance(healthInsurance).build();
        entityManager.persist(patient);

        Medication medication = Medication.builder().medicationName("medication").build();
        entityManager.persist(medication);

        Prescription prescription1 = Prescription.builder().medication(medication).patient(patient).doctor(generalPractitioner)
                .amount(1).prescribedAt(LocalDateTime.of(2022, 4, 5, 8, 12)).build();
        Prescription prescription2 = Prescription.builder().medication(medication).patient(patient).doctor(generalPractitioner)
                .amount(1).prescribedAt(LocalDateTime.of(2022, 5, 5, 8, 12)).build();
        Prescription prescription3 = Prescription.builder().medication(medication).patient(patient).doctor(generalPractitioner)
                .amount(1).prescribedAt(LocalDateTime.of(2022, 6, 5, 8, 12)).build();
        Prescription prescription4 = Prescription.builder().medication(medication).patient(patient).doctor(generalPractitioner)
                .amount(1).prescribedAt(LocalDateTime.of(2022, 7, 5, 8, 12)).build();
        Prescription prescription5 = Prescription.builder().medication(medication).patient(patient).doctor(generalPractitioner)
                .amount(1).prescribedAt(LocalDateTime.of(2022, 8, 5, 8, 12)).build();
        Prescription prescription6 = Prescription.builder().medication(medication).patient(patient).doctor(generalPractitioner)
                .amount(1).prescribedAt(LocalDateTime.of(2022, 9, 5, 8, 12)).build();
        Prescription prescription7 = Prescription.builder().medication(medication).patient(patient).doctor(generalPractitioner)
                .amount(1).prescribedAt(LocalDateTime.of(2022, 10, 5, 8, 12)).build();
        Prescription prescription8 = Prescription.builder().medication(medication).patient(patient).doctor(generalPractitioner)
                .amount(1).prescribedAt(LocalDateTime.of(2022, 11, 5, 8, 12)).build();
        Prescription prescription9 = Prescription.builder().medication(medication).patient(patient).doctor(generalPractitioner)
                .amount(1).prescribedAt(LocalDateTime.of(2022, 12, 5, 8, 12)).build();
        Prescription prescription10 = Prescription.builder().medication(medication).patient(patient).doctor(generalPractitioner)
                .amount(1).prescribedAt(LocalDateTime.of(2023, 1, 5, 8, 12)).build();
        Prescription prescription11 = Prescription.builder().medication(medication).patient(patient).doctor(generalPractitioner)
                .amount(1).prescribedAt(LocalDateTime.of(2023, 2, 5, 8, 12)).build();
        Prescription prescription12 = Prescription.builder().medication(medication).patient(patient).doctor(generalPractitioner)
                .amount(1).prescribedAt(LocalDateTime.of(2023, 3, 5, 8, 12)).build();
        Prescription prescription13 = Prescription.builder().medication(medication).patient(patient).doctor(generalPractitioner)
                .amount(1).prescribedAt(LocalDateTime.of(2023, 3, 5, 8, 12)).build();
        entityManager.persist(prescription1);
        entityManager.persist(prescription2);
        entityManager.persist(prescription3);
        entityManager.persist(prescription4);
        entityManager.persist(prescription5);
        entityManager.persist(prescription6);
        entityManager.persist(prescription7);
        entityManager.persist(prescription8);
        entityManager.persist(prescription9);
        entityManager.persist(prescription10);
        entityManager.persist(prescription11);
        entityManager.persist(prescription12);
        entityManager.persist(prescription13);

        List<Object[]> doctorsPrescriptionsByMonth = underTest.getDoctorPrescriptionCountByMonth(generalPractitioner,
                LocalDateTime.of(2022, 4, 1, 0, 0),
                LocalDateTime.of(2023, 3, 31, 23, 59));
        Object[] countsDoctor = doctorsPrescriptionsByMonth.stream().map(row -> row[0]).toArray();
        assertArrayEquals(new Long[]{1L, 1L, 2L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L}, countsDoctor);

        List<Object[]> patientsPrescriptionsByMonth = underTest.getPatientsPrescriptionCountByMonth(patient,
                LocalDateTime.of(2022, 4, 1, 0, 0),
                LocalDateTime.of(2023, 3, 31, 23, 59));
        Object[] countsPatient = patientsPrescriptionsByMonth.stream().map(row -> row[0]).toArray();
        assertArrayEquals(new Long[]{1L, 1L, 2L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L}, countsPatient);
    }

    @Test
    void getPatientsPrescriptionCountByMonth() {
    }
}