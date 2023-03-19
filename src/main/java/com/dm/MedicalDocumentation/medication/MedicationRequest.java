package com.dm.MedicalDocumentation.medication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicationRequest {
    private String name;
    private int amount;
    private String unit;
}
