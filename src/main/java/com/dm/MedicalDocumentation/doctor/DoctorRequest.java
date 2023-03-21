package com.dm.MedicalDocumentation.doctor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRequest {
    private String userLogin;
    private String person;
    private String hospital;
    private String departmentType;
}
