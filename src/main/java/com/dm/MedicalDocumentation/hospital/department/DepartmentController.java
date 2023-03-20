package com.dm.MedicalDocumentation.hospital.department;

import com.dm.MedicalDocumentation.exception.RecordAlreadyExistsException;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class DepartmentController {
    private final DepartmentService service;

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<Object> createHospital(
            @RequestBody DepartmentRequest request
    ) {
        if (service.recordExists(request)) {
            throw new RecordAlreadyExistsException("A department: " + request.getDepartmentType() + " in hospital: "
                    + request.getHospital() + ", already exists.");
        }
        service.createDepartment(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
