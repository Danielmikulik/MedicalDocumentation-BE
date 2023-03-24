package com.dm.MedicalDocumentation.patient;

import com.dm.MedicalDocumentation.GlobalConstants;
import com.dm.MedicalDocumentation.config.JwtService;
import com.dm.MedicalDocumentation.exception.RecordAlreadyExistsException;
import com.dm.MedicalDocumentation.request.StringRequest;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PatientController {
    private final JwtService jwtService;
    private final PatientService service;

    @PostMapping("/name")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<String> getPatientByBirthNumber(
            @RequestBody StringRequest request
    ) {
        return ResponseEntity.ok(service.getPatientNameByBirthNumber(request.getValue()));
    }

    @GetMapping("/doctors_patients")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<List<String>> getDoctorsPatients(
            @RequestHeader (name="Authorization") String token
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        String department = (String) jwtService.extractClaim(token.substring(7), "department");
        boolean isGeneralPractitioner = department.equalsIgnoreCase(GlobalConstants.GENERAL_PRACTITIONERS_CLINIC);
        return ResponseEntity.ok(service.getDoctorsPatients(userLogin, isGeneralPractitioner));
    }

    @GetMapping("/all")
    @RolesAllowed("ADMIN")
    public ResponseEntity<List<String>> getAllPatients(
    ) {
        return ResponseEntity.ok(service.getAllPatients());
    }

    @GetMapping("/doctor_count")
    @RolesAllowed("PATIENT")
    public ResponseEntity<Integer> getPatientsDoctorsCount(
            @RequestHeader (name="Authorization") String token
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getPatientsDoctorsCount(userLogin));
    }

    @PostMapping("/health_insurance_change")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Object> changeHealthInsurance(
            @RequestBody PatientInsuranceChangeRequest request
    ) {
        if (service.healthInsuranceEquals(request)) {
            throw new RecordAlreadyExistsException("Patient is already registered at given health insurance.");
        }
        service.changeHealthInsurance(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<String> createPerson(
            @RequestBody PatientRequest request
    ) {
        if (service.recordExists(request)) {
            throw new RecordAlreadyExistsException("A person with birth number: " + request.getPerson() + ", already is a patient.");
        }
        service.createPatient(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
