package com.dm.MedicalDocumentation.medication;

import com.dm.MedicalDocumentation.exception.RecordAlreadyExistsException;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medication")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "https://localhost:3000"})
public class MedicationController {
    private final MedicationService service;

    @GetMapping("/all")
    @RolesAllowed({"DOCTOR", "ADMIN"})
    public ResponseEntity<List<String>> getMedications() {
        return ResponseEntity.ok(service.getMedications());
    }

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<Object> createMedication(
            @RequestBody MedicationRequest request
    ) {
        if (service.recordExists(request.getName(), request.getAmount())) {
            throw new RecordAlreadyExistsException("A medication with name: " + request.getName() + ", already exists.");
        }
        service.createMedication(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
