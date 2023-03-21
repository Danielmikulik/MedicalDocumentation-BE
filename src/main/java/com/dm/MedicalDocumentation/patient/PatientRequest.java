package com.dm.MedicalDocumentation.patient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequest {
    private String userLogin;
    private String person;
    private String generalPractitioner;
    private String healthInsurance;
}
