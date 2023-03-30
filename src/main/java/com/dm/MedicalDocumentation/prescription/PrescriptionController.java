package com.dm.MedicalDocumentation.prescription;

import com.dm.MedicalDocumentation.config.JwtService;
import com.dm.MedicalDocumentation.request.StringRequest;
import com.dm.MedicalDocumentation.response.CountsByMonthResponse;
import com.dm.MedicalDocumentation.response.PrescriptionResponse;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prescription")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PrescriptionController {
    private final JwtService jwtService;
    private final PrescriptionService service;
    @PostMapping("/patient_prescriptions")
    @RolesAllowed("PATIENT")
    public ResponseEntity<List<PrescriptionResponse>> getPatientsPrescriptions(
            @RequestHeader (name="Authorization") String token,
            @RequestBody Map<String, String> medicationMap
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getPatientsPrescriptions(userLogin, medicationMap.get("medication")));
    }

    @GetMapping("/patient_medications")
    @RolesAllowed("PATIENT")
    public ResponseEntity<List<String>> getPatientsMedications(
            @RequestHeader (name="Authorization") String token
            ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getPatientsMedicationsByUserLogin(userLogin));
    }

    @PostMapping("/patient_medications_by_birth_number")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<List<String>> getPatientsMedicationsByBirthNumber(
            @RequestBody StringRequest request
    ) {
        return ResponseEntity.ok(service.getPatientsMedicationsByBirthNumber(request.getValue()));
    }

    @PostMapping
    @RolesAllowed("DOCTOR")
    public ResponseEntity<Object> createPrescription(
            @RequestHeader (name="Authorization") String token,
            @RequestBody PrescriptionRequest request
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        service.createPrescription(userLogin, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/doctor_total_count")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<Long> getDoctorsTotalPrescriptionCount(
            @RequestHeader (name="Authorization") String token
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getDoctorsTotalPrescriptionCount(userLogin, true));
    }

    @GetMapping("/doctor_stats")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<CountsByMonthResponse> getDoctorsPrescriptionCountsForLastYear(
            @RequestHeader (name="Authorization") String token,
            @RequestParam LocalDate dateSince,
            @RequestParam LocalDate dateUntil,
            @RequestParam String interval
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getPrescriptionCountsForLastYear(userLogin, dateSince, dateUntil, interval, true));
    }

    @GetMapping("/patient_stats")
    @RolesAllowed("PATIENT")
    public ResponseEntity<CountsByMonthResponse> getPatientsPrescriptionCountsForLastYear(
            @RequestHeader (name="Authorization") String token,
            @RequestParam LocalDate dateSince,
            @RequestParam LocalDate dateUntil,
            @RequestParam String interval
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getPrescriptionCountsForLastYear(userLogin, dateSince, dateUntil, interval, false));
    }

    @GetMapping("/patient_total_count")
    @RolesAllowed("PATIENT")
    public ResponseEntity<Long> getPatientsTotalPrescriptionCount(
            @RequestHeader (name="Authorization") String token
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getDoctorsTotalPrescriptionCount(userLogin, false));
    }

    @GetMapping("/patient_to_retrieve")
    @RolesAllowed("PATIENT")
    public ResponseEntity<Long> getPatientsPrescriptionCountToRetrieve(
            @RequestHeader (name="Authorization") String token
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getPatientsPrescriptionCountToRetrieve(userLogin));
    }
}
