package com.dm.MedicalDocumentation.user;

import com.dm.MedicalDocumentation.config.JwtService;
import com.dm.MedicalDocumentation.response.userInfo.AdminInfoResponse;
import com.dm.MedicalDocumentation.response.userInfo.DoctorInfoResponse;
import com.dm.MedicalDocumentation.response.userInfo.HospitalInfoResponse;
import com.dm.MedicalDocumentation.response.userInfo.PatientInfoResponse;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final JwtService jwtService;
    private final UserService service;

    @PostMapping("/patient")
    @RolesAllowed("PATIENT")
    public ResponseEntity<PatientInfoResponse> getPatientInfo(
            @RequestHeader (name="Authorization") String token
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getPatientInfo(userLogin));
    }

    @PostMapping("/doctor")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<DoctorInfoResponse> getDoctorInfo(
            @RequestHeader (name="Authorization") String token
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getDoctorInfo(userLogin));
    }

    @PostMapping("/hospital")
    @RolesAllowed("HOSPITAL")
    public ResponseEntity<HospitalInfoResponse> getHospitalInfo(
            @RequestHeader (name="Authorization") String token
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getHospitalInfo(userLogin));
    }

    @PostMapping("/admin")
    @RolesAllowed("ADMIN")
    public ResponseEntity<AdminInfoResponse> getAdminInfo(
            @RequestHeader (name="Authorization") String token
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getAdminInfo(userLogin));
    }
}
