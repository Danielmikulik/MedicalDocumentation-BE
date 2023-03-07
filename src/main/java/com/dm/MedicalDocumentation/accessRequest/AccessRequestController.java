package com.dm.MedicalDocumentation.accessRequest;

import com.dm.MedicalDocumentation.config.JwtService;
import com.dm.MedicalDocumentation.response.AccessRequestResponse;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/access_request")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AccessRequestController {
    private final JwtService jwtService;
    private final AccessRequestService service;

    @PostMapping("/patient")
    @RolesAllowed("PATIENT")
    public ResponseEntity<List<AccessRequestResponse>> getPatientsMedicalExams(
            @RequestHeader(name="Authorization") String token
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getPatientsAccessRequests(userLogin));
    }
}
