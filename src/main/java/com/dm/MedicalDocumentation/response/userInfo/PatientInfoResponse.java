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
public class PatientInfoResponse {
    @JsonProperty("Meno a priezvisko")
    private String fullName;
    @JsonProperty("Rodné číslo")
    private String birthNumber;
    @JsonProperty("Praktický lekár")
    private String generalPractitioner;
    @JsonProperty("Zdravotná poisťovňa")
    private String insurance;
    @JsonProperty("Prihlasovacie meno")
    private String userLogin;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("Telefón")
    private String telephone;
    @JsonProperty("Mesto")
    private String city;
    @JsonProperty("PSČ")
    private String zipCode;
    @JsonProperty("Adresa")
    private String address;
    @JsonFormat(pattern="dd.MM.yyyy HH:mm")
    @JsonProperty("Registrovaný od")
    private LocalDateTime registeredSince;
}
