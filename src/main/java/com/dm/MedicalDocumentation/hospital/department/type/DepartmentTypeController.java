package com.dm.MedicalDocumentation.hospital.department.type;

import com.dm.MedicalDocumentation.exception.RecordAlreadyExistsException;
import com.dm.MedicalDocumentation.request.StringRequest;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department_type")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class DepartmentTypeController {
    private final DepartmentTypeService service;

    @GetMapping("/all")
    @RolesAllowed({"DOCTOR", "ADMIN", "HOSPITAL"})
    public ResponseEntity<List<String>> getDepartmentTypes() {
        return ResponseEntity.ok(service.getDepartments());
    }

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<Object> createDepartmentType(
            @RequestBody StringRequest request
    ) {
        if (service.recordExists(request.getValue())) {
            throw new RecordAlreadyExistsException("A department type: " + request.getValue() + ", already exists.");
        }
        service.createDepartmentType(request.getValue());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
