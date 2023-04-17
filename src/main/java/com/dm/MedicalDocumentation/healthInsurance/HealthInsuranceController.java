package com.dm.MedicalDocumentation.healthInsurance;

import com.dm.MedicalDocumentation.exception.RecordAlreadyExistsException;
import com.dm.MedicalDocumentation.request.StringRequest;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/health_insurance")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "https://localhost:3000"})
public class HealthInsuranceController {
    private final HealthInsuranceService service;

    @GetMapping("/all")
    @RolesAllowed({"DOCTOR", "ADMIN"})
    public ResponseEntity<List<String>> getHealthInsurances() {
        return ResponseEntity.ok(service.getHealthInsurances());
    }

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<Object> createHealthInsurance(
            @RequestBody StringRequest request
    ) {
        if (service.recordExists(request.getValue())) {
            throw new RecordAlreadyExistsException("A health insurance with name: " + request.getValue() + ", already exists.");
        }
        service.createHealthInsurance(request.getValue());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
