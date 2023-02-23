package com.dm.MedicalDocumentation.response;

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
public class PatientInfoResponse {
    @JsonProperty("Prihlasovacie meno")
    private String userLogin;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("Telefón")
    private String telephone;
    @JsonProperty("Registrovaný od")
    private LocalDateTime registeredSince;
    @JsonProperty("Meno a priezvisko")
    private String fullName;
    @JsonProperty("Rodné číslo")
    private String birthNumber;
    @JsonProperty("Mesto")
    private String city;
    @JsonProperty("Adresa")
    private String address;
    @JsonProperty("Zdravotná poisťovňa")
    private String insurance;
}
