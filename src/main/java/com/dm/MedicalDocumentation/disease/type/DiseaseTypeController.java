package com.dm.MedicalDocumentation.disease.type;

import com.dm.MedicalDocumentation.exception.RecordAlreadyExistsException;
import com.dm.MedicalDocumentation.request.StringRequest;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disease_type")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class DiseaseTypeController {
    private final DiseaseTypeService service;
    @GetMapping("/all")
    @RolesAllowed({"DOCTOR", "ADMIN", "HOSPITAL"})
    public ResponseEntity<List<String>> getDepartmentTypes() {
        return ResponseEntity.ok(service.getDiseaseTypes());
    }
    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<Object> createDiseaseType(
            @RequestBody StringRequest request
    ) {
        if (service.recordExists(request.getValue())) {
            throw new RecordAlreadyExistsException("A disease type: " + request.getValue() + ", already exists.");
        }
        service.createDiseaseType(request.getValue());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
