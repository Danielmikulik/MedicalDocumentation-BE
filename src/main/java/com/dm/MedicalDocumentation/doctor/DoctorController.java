package com.dm.MedicalDocumentation.doctor;

import com.dm.MedicalDocumentation.attachment.Attachment;
import com.dm.MedicalDocumentation.config.JwtService;
import com.dm.MedicalDocumentation.response.userInfo.PublicDoctorInfoResponse;
import com.dm.MedicalDocumentation.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class DoctorController {
    private final DoctorService service;

    @GetMapping("/{doctorId}")
    public ResponseEntity<PublicDoctorInfoResponse> getDoctorInfo(
            @RequestHeader(name="Authorization") String token,
            @PathVariable("doctorId") long doctorId
    ) {
        return ResponseEntity.ok(service.getDoctorInfo(doctorId));
    }
}
