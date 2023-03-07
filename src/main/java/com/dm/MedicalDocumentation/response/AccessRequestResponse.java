package com.dm.MedicalDocumentation.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessRequestResponse {
    private String disease;
    private LocalDateTime time;
    private String examDoctor;
    private String requestDoctor;
    private LocalDateTime accessibleUntil;
}
