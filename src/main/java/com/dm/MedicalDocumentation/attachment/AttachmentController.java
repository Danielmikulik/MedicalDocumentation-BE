package com.dm.MedicalDocumentation.attachment;

import com.dm.MedicalDocumentation.config.JwtService;
import com.dm.MedicalDocumentation.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/api/attachment")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AttachmentController {
    private final JwtService jwtService;
    private final AttachmentService service;

    @GetMapping("/{examId}")
    public ResponseEntity<List<Attachment>> getPatientsMedicalExams(
            @RequestHeader(name="Authorization") String token,
            @PathVariable("examId") int examId
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        ArrayList<LinkedHashMap<String, String>> authorities = (ArrayList<LinkedHashMap<String, String>>) jwtService.extractClaim(token.substring(7), "Authorities");
        Role role = Role.valueOf(authorities.get(0).get("authority"));

        return ResponseEntity.ok(service.getAttachmentForMedExam(examId, userLogin, role));
    }
}
