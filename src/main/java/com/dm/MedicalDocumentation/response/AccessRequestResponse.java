package com.dm.MedicalDocumentation.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessRequestResponse {
    private long id;
    private String patientName;
    private String patientBirthNumber;
    private String requestDoctor;
    private String examDoctor;
    private String department;
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    private LocalDateTime startTime;
    @JsonFormat(pattern="dd.MM.yyyy")
    private LocalDate accessibleUntil;
}
