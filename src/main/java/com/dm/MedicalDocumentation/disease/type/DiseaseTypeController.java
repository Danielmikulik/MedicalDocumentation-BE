package com.dm.MedicalDocumentation.disease.type;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
