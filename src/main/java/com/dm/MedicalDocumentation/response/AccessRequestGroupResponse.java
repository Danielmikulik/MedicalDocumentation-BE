package com.dm.MedicalDocumentation.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessRequestGroupResponse {
    private long count;
    private String patientName;
    private String patientBirthNumber;
    private String department;
    private boolean rejected;
}
