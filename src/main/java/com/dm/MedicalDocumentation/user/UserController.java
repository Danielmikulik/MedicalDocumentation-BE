package com.dm.MedicalDocumentation.user;

import com.dm.MedicalDocumentation.config.JwtService;
import com.dm.MedicalDocumentation.response.CountsByMonthResponse;
import com.dm.MedicalDocumentation.response.userInfo.AdminInfoResponse;
import com.dm.MedicalDocumentation.response.userInfo.DoctorInfoResponse;
import com.dm.MedicalDocumentation.response.userInfo.PatientInfoResponse;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "https://localhost:3000"})
public class UserController {

    private final JwtService jwtService;
    private final UserService service;

    @GetMapping("/patient")
    @RolesAllowed("PATIENT")
    public ResponseEntity<PatientInfoResponse> getPatientInfo(
            @RequestHeader (name="Authorization") String token
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getPatientInfo(userLogin));
    }

    @GetMapping("/doctor")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<DoctorInfoResponse> getDoctorInfo(
            @RequestHeader (name="Authorization") String token
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getDoctorInfo(userLogin));
    }

    @GetMapping("/admin")
    @RolesAllowed("ADMIN")
    public ResponseEntity<AdminInfoResponse> getAdminInfo(
            @RequestHeader (name="Authorization") String token
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getAdminInfo(userLogin));
    }

    @GetMapping("/pharmacy")
    @RolesAllowed("PHARMACY")
    public ResponseEntity<PharmacyInfoResponse> getPharmacyInfo(
            @RequestHeader (name="Authorization") String token
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getPharmacyInfo(userLogin));
    }

    @GetMapping("/roles")
    @RolesAllowed("ADMIN")
    public ResponseEntity<List<String>> getRoles() {
        return ResponseEntity.ok(service.getRoles());
    }

    @GetMapping("/logins")
    @RolesAllowed("ADMIN")
    public ResponseEntity<List<String>> getLogins() {
        return ResponseEntity.ok(service.getLogins());
    }

    @GetMapping("/unused_pharmacy_logins")
    @RolesAllowed("ADMIN")
    public ResponseEntity<List<String>> getUnusedPharmacyLogins() {
        return ResponseEntity.ok(service.getUnusedPharmacyLogins());
    }

    @GetMapping("/unused_patient_logins")
    @RolesAllowed("ADMIN")
    public ResponseEntity<List<String>> getUnusedPatientLogins() {
        return ResponseEntity.ok(service.getUnusedPatientLogins());
    }

    @GetMapping("/unused_doctor_logins")
    @RolesAllowed("ADMIN")
    public ResponseEntity<List<String>> getUnusedDoctorLogins() {
        return ResponseEntity.ok(service.getUnusedDoctorLogins());
    }

    @GetMapping("/created_last_year")
    @RolesAllowed("ADMIN")
    public ResponseEntity<CountsByMonthResponse> getCreatedUserCountsForInterval(
            @RequestParam LocalDate dateSince,
            @RequestParam LocalDate dateUntil,
            @RequestParam String interval
    ) {
        return ResponseEntity.ok(service.getCreatedUserCountsForInterval(dateSince, dateUntil, interval));
    }
}
