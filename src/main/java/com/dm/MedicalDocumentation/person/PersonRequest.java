package com.dm.MedicalDocumentation.person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonRequest {
    private String name;
    private String surname;
    private String birthNumber;
    private LocalDate birthDate;
    private String zipCode;
    private String address;
}
