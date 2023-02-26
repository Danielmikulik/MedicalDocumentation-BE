package com.dm.MedicalDocumentation.response.userInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminInfoResponse {
    @JsonProperty("Prihlasovacie meno")
    private String userLogin;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("Telefón")
    private String telephone;
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    @JsonProperty("Registrovaný od")
    private LocalDateTime registeredSince;
}
