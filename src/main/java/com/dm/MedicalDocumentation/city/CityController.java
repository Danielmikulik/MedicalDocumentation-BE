package com.dm.MedicalDocumentation.city;

import com.dm.MedicalDocumentation.exception.RecordAlreadyExistsException;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/city")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CityController {
    private final CityService service;

    @GetMapping("/all")
    @RolesAllowed("ADMIN")
    public ResponseEntity<List<String>> getDiseaseTypes() {
        return ResponseEntity.ok(service.getCities());
    }

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<Object> createCity(
            @RequestBody CityRequest request
    ) {
        if (service.recordExists(request.getZipCode())) {
            throw new RecordAlreadyExistsException("A city with code: " + request.getZipCode() + ", already exists.");
        }
        service.createCity(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
