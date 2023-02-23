package com.dm.MedicalDocumentation.user;

import com.dm.MedicalDocumentation.request.UserLoginRequest;
import com.dm.MedicalDocumentation.response.AdminInfoResponse;
import com.dm.MedicalDocumentation.response.DoctorInfoResponse;
import com.dm.MedicalDocumentation.response.HospitalInfoResponse;
import com.dm.MedicalDocumentation.response.PatientInfoResponse;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService service;

    @PostMapping("/patient")
    @RolesAllowed("PATIENT")
    public ResponseEntity<PatientInfoResponse> getPatientInfo(
            @RequestBody UserLoginRequest request
    ) {
        return ResponseEntity.ok(service.getPatientInfo(request));
    }

    @PostMapping("/doctor")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<DoctorInfoResponse> getDoctorInfo(
            @RequestBody UserLoginRequest request
    ) {
        return ResponseEntity.ok(service.getDoctorInfo(request));
    }

    @PostMapping("/hospital")
    @RolesAllowed("HOSPITAL")
    public ResponseEntity<HospitalInfoResponse> getHospitalInfo(
            @RequestBody UserLoginRequest request
    ) {
        return ResponseEntity.ok(service.getHospitalInfo(request));
    }

    @PostMapping("/admin")
    @RolesAllowed("ADMIN")
    public ResponseEntity<AdminInfoResponse> getAdminInfo(
            @RequestBody UserLoginRequest request
    ) {
        return ResponseEntity.ok(service.getAdminInfo(request));
    }
}
