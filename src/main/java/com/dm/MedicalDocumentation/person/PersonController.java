package com.dm.MedicalDocumentation.person;

import com.dm.MedicalDocumentation.exception.RecordAlreadyExistsException;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PersonController {
    private final PersonService service;

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<String> createPerson(
            @RequestBody PersonRequest request
    ) {
        if (service.recordExists(request.getBirthNumber())) {
            throw new RecordAlreadyExistsException("A person with birth number: " + request.getBirthNumber() + ", already exists.");
        }
        service.createPerson(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
