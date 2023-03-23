package com.dm.MedicalDocumentation.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MedicalExamRequest {
    private String patient;
    private String examinationType;
    private String diseaseType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String report;
    private MultipartFile file;
}
