package com.dm.MedicalDocumentation.doctor;

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
}
