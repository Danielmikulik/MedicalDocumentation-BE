package com.dm.MedicalDocumentation.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalExamResponse {
    private Long id;
    private String type;
    private String disease;
    private String patient;
    private String department;
    private String doctor;
    private Long doctorId;
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    private LocalDateTime startTime;
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    private LocalDateTime endTime;
}
