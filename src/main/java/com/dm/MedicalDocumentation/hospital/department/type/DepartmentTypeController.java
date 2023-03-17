package com.dm.MedicalDocumentation.hospital.department.type;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
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
}
