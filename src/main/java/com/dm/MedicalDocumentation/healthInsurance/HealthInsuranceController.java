package com.dm.MedicalDocumentation.healthInsurance;

import com.dm.MedicalDocumentation.exception.RecordAlreadyExistsException;
import com.dm.MedicalDocumentation.request.StringRequest;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health_insurance")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class HealthInsuranceController {
    private final HealthInsuranceService service;

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
