package com.dm.MedicalDocumentation.pharmacy;

import com.dm.MedicalDocumentation.exception.RecordAlreadyExistsException;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pharmacy")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "https://localhost:3000"})
public class PharmacyController {
    private final PharmacyService service;
    @GetMapping("/count")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Long> getPharmacyCount(
            @RequestParam String city
    ) {
        return ResponseEntity.ok(service.getPharmacyCount(city));
    }

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<Object> createPharmacy(
            @RequestBody PharmacyRequest request
    ) {
        if (service.recordExists(request)) {
            throw new RecordAlreadyExistsException("Given pharmacy already exists.");
        }
        service.createPharmacy(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
