package com.dm.MedicalDocumentation.pharmacy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PharmacyRequest {
    private String name;
    private String userLogin;
    private String zipCode;
    private String address;
}
