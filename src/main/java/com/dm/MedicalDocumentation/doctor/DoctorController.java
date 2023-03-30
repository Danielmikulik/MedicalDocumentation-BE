package com.dm.MedicalDocumentation.doctor;

import com.dm.MedicalDocumentation.config.JwtService;
import com.dm.MedicalDocumentation.exception.RecordAlreadyExistsException;
import com.dm.MedicalDocumentation.response.userInfo.PublicDoctorInfoResponse;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class DoctorController {
    private final JwtService jwtService;
    private final DoctorService service;

    @GetMapping("/all")
    @RolesAllowed("ADMIN")
    public ResponseEntity<List<String>> getAllDoctors() {
        return ResponseEntity.ok(service.getAllDoctors());
    }

    @GetMapping("/general_practitioners")
    @RolesAllowed("ADMIN")
    public ResponseEntity<List<String>> getAllGeneralPractitioners() {
        return ResponseEntity.ok(service.getAllGeneralPractitioners());
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<PublicDoctorInfoResponse> getDoctorInfo(
            @PathVariable("doctorId") long doctorId
    ) {
        return ResponseEntity.ok(service.getDoctorInfo(doctorId));
    }

    @GetMapping("/patient_count")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<Integer> getDoctorsPatientCount(
            @RequestHeader (name="Authorization") String token,
            @RequestParam String diseaseType
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getDoctorsPatientCount(userLogin, diseaseType));
    }

    @PostMapping("/department_change")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Object> changeDepartment(
            @RequestBody DoctorChangeRequest request
    ) {
        if (service.departmentEquals(request)) {
            throw new RecordAlreadyExistsException("Doctor already works at given department.");
        }
        service.changeDepartment(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<String> createPerson(
            @RequestBody DoctorRequest request
    ) {
        if (service.recordExists(request)) {
            throw new RecordAlreadyExistsException("A person with birth number: " + request.getPerson() + ", already is a doctor.");
        }
        service.createDoctor(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/count")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Long> getDoctorCount() {
        return ResponseEntity.ok(service.getDoctorCount());
    }
}
