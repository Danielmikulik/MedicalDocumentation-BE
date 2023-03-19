package com.dm.MedicalDocumentation.hospital;

import com.dm.MedicalDocumentation.exception.RecordAlreadyExistsException;
import com.dm.MedicalDocumentation.request.StringRequest;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hospital")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class HospitalController {
    private final HospitalService service;

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<Object> createExaminationType(
            @RequestBody StringRequest request
    ) {
        if (service.recordExists(request.getValue())) {
            throw new RecordAlreadyExistsException("A Hospital with name: " + request.getValue() + ", already exists.");
        }
        service.createHospital(request.getValue());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
