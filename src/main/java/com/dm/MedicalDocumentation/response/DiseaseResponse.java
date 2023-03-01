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
public class DiseaseResponse {
    private String disease;
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    private LocalDateTime diagnosed;
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    private LocalDateTime cured;
}
