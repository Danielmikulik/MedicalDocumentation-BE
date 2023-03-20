package com.dm.MedicalDocumentation.patient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientInsuranceChangeRequest {
    private String patient;
    private String healthInsurance;
}
