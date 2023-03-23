package com.dm.MedicalDocumentation.doctor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorChangeRequest {
    private String doctor;
    private String hospital;
    private String departmentType;
    private LocalDate changeDate;
}
