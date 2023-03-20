package com.dm.MedicalDocumentation.hospital.department;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequest {
    private String hospital;
    private String departmentType;
}
