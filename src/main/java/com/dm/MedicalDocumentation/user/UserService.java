package com.dm.MedicalDocumentation.user;

import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.hospital.Hospital;
import com.dm.MedicalDocumentation.hospital.department.Department;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentType;
import com.dm.MedicalDocumentation.patient.Patient;
import com.dm.MedicalDocumentation.person.Person;
import com.dm.MedicalDocumentation.response.userInfo.AdminInfoResponse;
import com.dm.MedicalDocumentation.response.userInfo.DoctorInfoResponse;
import com.dm.MedicalDocumentation.response.userInfo.PatientInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public PatientInfoResponse getPatientInfo(String userLogin) {
        User user = repository.findByUserLogin(userLogin)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userLogin"));
        Patient patient = user.getPatient();
        Person person = patient.getPerson();
        return PatientInfoResponse.builder()
                .userLogin(user.getUserLogin())
                .email(user.getEmail())
                .telephone(user.getTelephone())
                .registeredSince(user.getCreatedAt())
                .fullName(person.getFullName())
                .birthNumber(person.getBirthNumber())
                .city(person.getCity().getCityName())
                .address(person.getAddress())
                .insurance(patient.getHealthInsurance().getInsuranceName())
                .build();
    }

    public DoctorInfoResponse getDoctorInfo(String userLogin) {
        User user = repository.findByUserLogin(userLogin)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userLogin"));
        Doctor doctor = user.getDoctor();
        Person person = doctor.getPerson();
        Department department = doctor.getDepartment();
        Hospital hospital = department.getId().getHospital();
        DepartmentType departmentType = department.getId().getDepartmentType();

        return DoctorInfoResponse.builder()
                .userLogin(user.getUserLogin())
                .email(user.getEmail())
                .telephone(user.getTelephone())
                .registeredSince(user.getCreatedAt())
                .fullName(person.getFullName())
                .birthNumber(person.getBirthNumber())
                .city(person.getCity().getCityName())
                .address(person.getAddress())
                .hospital(hospital.getHospitalName())
                .department(departmentType.getDepartmentTypeName())
                .build();
    }

    public AdminInfoResponse getAdminInfo(String userLogin) {
        User user = repository.findByUserLogin(userLogin)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userLogin"));
        return AdminInfoResponse.builder()
                .userLogin(user.getUserLogin())
                .email(user.getEmail())
                .telephone(user.getTelephone())
                .registeredSince(user.getCreatedAt())
                .build();
    }
}
