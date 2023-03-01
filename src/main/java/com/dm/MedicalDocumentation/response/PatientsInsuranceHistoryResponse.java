package com.dm.MedicalDocumentation.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientsInsuranceHistoryResponse {
    private String insurance;
    @JsonFormat(pattern="dd.MM.yyyy")
    private LocalDate since;
    @JsonFormat(pattern="dd.MM.yyyy")
    private LocalDate till;
}
