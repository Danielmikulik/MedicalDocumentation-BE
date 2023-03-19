package com.dm.MedicalDocumentation.medicalExamination.type;

import com.dm.MedicalDocumentation.exception.RecordAlreadyExistsException;
import com.dm.MedicalDocumentation.request.StringRequest;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/examination_type")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ExaminationTypeController {
    private final ExaminationTypeService service;

    @GetMapping("/all")
    @RolesAllowed({"DOCTOR", "ADMIN"})
    public ResponseEntity<List<String>> getExaminationTypes() {
        return ResponseEntity.ok(service.getExaminationTypes());
    }

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<Object> createExaminationType(
            @RequestBody StringRequest request
    ) {
        if (service.recordExists(request.getValue())) {
            throw new RecordAlreadyExistsException("A examination type: " + request.getValue() + ", already exists.");
        }
        service.createExaminationType(request.getValue());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
