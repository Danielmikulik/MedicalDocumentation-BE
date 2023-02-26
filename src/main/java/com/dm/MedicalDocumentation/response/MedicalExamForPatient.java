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
public class MedicalExamForPatient {
    private String type;
    private String disease;
    private String doctor;
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    private LocalDateTime startTime;
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    private LocalDateTime endTime;
}
