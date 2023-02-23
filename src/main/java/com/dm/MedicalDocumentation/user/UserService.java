package com.dm.MedicalDocumentation.user;

import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.hospital.Hospital;
import com.dm.MedicalDocumentation.hospital.department.Department;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentType;
import com.dm.MedicalDocumentation.patient.Patient;
import com.dm.MedicalDocumentation.person.Person;
import com.dm.MedicalDocumentation.request.UserLoginRequest;
import com.dm.MedicalDocumentation.response.AdminInfoResponse;
import com.dm.MedicalDocumentation.response.DoctorInfoResponse;
import com.dm.MedicalDocumentation.response.HospitalInfoResponse;
import com.dm.MedicalDocumentation.response.PatientInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public PatientInfoResponse getPatientInfo(UserLoginRequest request) {
        User user = repository.findByUserLogin(request.getUserLogin())
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

    public DoctorInfoResponse getDoctorInfo(UserLoginRequest request) {
        User user = repository.findByUserLogin(request.getUserLogin())
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

    public HospitalInfoResponse getHospitalInfo(UserLoginRequest request) {
        User user = repository.findByUserLogin(request.getUserLogin())
                .orElseThrow(() -> new IllegalArgumentException("Invalid userLogin"));
        Hospital hospital = user.getHospital();
        List<String> departments = hospital.getDepartments().stream()
                .map(department -> department.getId().getDepartmentType().getDepartmentTypeName())
                .toList();
        return HospitalInfoResponse.builder()
                .userLogin(user.getUserLogin())
                .email(user.getEmail())
                .telephone(user.getTelephone())
                .registeredSince(user.getCreatedAt())
                .departments(departments)
                .build();
    }

    public AdminInfoResponse getAdminInfo(UserLoginRequest request) {
        User user = repository.findByUserLogin(request.getUserLogin())
                .orElseThrow(() -> new IllegalArgumentException("Invalid userLogin"));
        return AdminInfoResponse.builder()
                .userLogin(user.getUserLogin())
                .email(user.getEmail())
                .telephone(user.getTelephone())
                .registeredSince(user.getCreatedAt())
                .build();
    }
}
