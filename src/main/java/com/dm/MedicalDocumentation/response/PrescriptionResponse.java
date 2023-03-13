package com.dm.MedicalDocumentation.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.Month;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionResponse {
    private String medicationName;
    private String medicationAmount;
    private int packageCount;
    private String doctor;
    private Long doctorId;
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    private LocalDateTime prescribedAt;
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    private LocalDateTime retrievedAt;
}
