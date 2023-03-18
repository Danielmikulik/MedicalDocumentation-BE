package com.dm.MedicalDocumentation.accessRequest;

import com.dm.MedicalDocumentation.config.JwtService;
import com.dm.MedicalDocumentation.request.AccessRequestRequest;
import com.dm.MedicalDocumentation.request.IdArrayRequest;
import com.dm.MedicalDocumentation.response.AccessRequestGroupResponse;
import com.dm.MedicalDocumentation.response.AccessRequestResponse;
import com.dm.MedicalDocumentation.response.CustomPage;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @PostMapping("/create")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<Integer> createAccessRequestsForPatientsMedExams(
            @RequestHeader(name="Authorization") String token,
            @RequestBody AccessRequestRequest request
            ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.createAccessRequestsForPatientsMedExams(userLogin, request));
    }

    @GetMapping("/show")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<List<AccessRequestGroupResponse>> getAccessRequestsByDoctor(
            @RequestHeader(name="Authorization") String token
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getAccessRequestsByDoctor(userLogin));
    }

    @GetMapping("/show_confirm")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<CustomPage<AccessRequestResponse>> getDoctorsPatientsAccessRequests(
            @RequestHeader(name="Authorization") String token,
            @RequestParam int pageIndex,
            @RequestParam int pageSize,
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) String patientBirthNumber,
            @RequestParam(required = false) String requestDoctor,
            @RequestParam(required = false) String examDoctor,
            @RequestParam(required = false) String department
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        Pageable page = PageRequest.of(pageIndex, pageSize);
        return ResponseEntity.ok(service.getDoctorsPatientsAccessRequests(userLogin, page, patientName,
                patientBirthNumber, requestDoctor, examDoctor, department));
    }

    @GetMapping("/doctors_patients")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<List<String>> getDoctorsPatientsWithAccessRequests(
            @RequestHeader(name="Authorization") String token
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getDoctorsPatientsWithAccessRequests(userLogin));
    }

    @PostMapping("/confirm")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<String> confirmAccessRequests(
            @RequestHeader(name="Authorization") String token,
            @RequestBody IdArrayRequest request
            ) {
        return updateAccessRequests(token, request, true);
    }

    @PostMapping("/reject")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<String> rejectAccessRequests(
            @RequestHeader(name="Authorization") String token,
            @RequestBody IdArrayRequest request
    ) {
        return updateAccessRequests(token, request, false);
    }

    private ResponseEntity<String> updateAccessRequests(String token, IdArrayRequest request, boolean isAccepted) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        String department = (String) jwtService.extractClaim(token.substring(7), "department");
        if (!department.equalsIgnoreCase("Ambulancia všeobecného lekára")) {
            return ResponseEntity.badRequest().body("Only general practitioners can confirm access requests");
        }
        return ResponseEntity.ok(service.updateAccessRequests(userLogin, request.getIds(), isAccepted));
    }
}
