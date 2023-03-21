package com.dm.MedicalDocumentation.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String userLogin;
    private String email;
    private String password;
    private String telephone;
    private String role;
}
