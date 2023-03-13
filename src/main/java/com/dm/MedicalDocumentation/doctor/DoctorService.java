package com.dm.MedicalDocumentation.doctor;

import com.dm.MedicalDocumentation.response.userInfo.PublicDoctorInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository repository;
    public PublicDoctorInfoResponse getDoctorInfo(long doctorId) {
        Doctor doctor = repository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("No doctor with given id found!"));
        return PublicDoctorInfoResponse.builder()
                .hospital(doctor.getDepartment().getId().getHospital().getHospitalName())
                .department(doctor.getDepartment().getId().getDepartmentType().getDepartmentTypeName())
                .email(doctor.getUser().getEmail())
                .build();
    }
}
