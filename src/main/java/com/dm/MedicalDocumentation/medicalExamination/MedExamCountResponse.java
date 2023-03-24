package com.dm.MedicalDocumentation.medicalExamination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedExamCountResponse {
    private List<Long> counts;
    private List<String> months;
}
