package com.dm.MedicalDocumentation.medicalExamination;

import com.dm.MedicalDocumentation.GlobalConstants;
import com.dm.MedicalDocumentation.config.JwtService;
import com.dm.MedicalDocumentation.request.MedicalExamRequest;
import com.dm.MedicalDocumentation.request.StringRequest;
import com.dm.MedicalDocumentation.response.CountsByMonthResponse;
import com.dm.MedicalDocumentation.response.CustomPage;
import com.dm.MedicalDocumentation.response.MedicalExamResponse;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/med_exams")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "https://localhost:3000"})
public class MedicalExaminationController {
    private final JwtService jwtService;
    private final MedicalExaminationService service;
    @GetMapping("/patient")
    @RolesAllowed("PATIENT")
    public ResponseEntity<CustomPage<MedicalExamResponse>> getPatientsMedicalExams(
            @RequestHeader (name="Authorization") String token,
            @RequestParam int pageIndex,
            @RequestParam int pageSize,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String doctor,
            @RequestParam(required = false) String department
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        Pageable page = PageRequest.of(pageIndex, pageSize);
        return ResponseEntity.ok(service.getPatientsExams(userLogin, type, doctor, department, page));
    }

    @GetMapping("/doctor_stats")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<CountsByMonthResponse> getDoctorsExamCountsForInterval(
            @RequestHeader (name="Authorization") String token,
            @RequestParam LocalDate dateSince,
            @RequestParam LocalDate dateUntil,
            @RequestParam String interval
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getExamCountsForInterval(userLogin, dateSince, dateUntil, interval, true));
    }

    @GetMapping("/patient_stats")
    @RolesAllowed("PATIENT")
    public ResponseEntity<CountsByMonthResponse> getPatientsExamCountsForInterval(
            @RequestHeader (name="Authorization") String token,
            @RequestParam LocalDate dateSince,
            @RequestParam LocalDate dateUntil,
            @RequestParam String interval
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getExamCountsForInterval(userLogin, dateSince, dateUntil, interval, false));
    }
    @GetMapping("/doctor_total_exam_count")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<Long> getDoctorsTotalExamCount(
            @RequestHeader (name="Authorization") String token,
            @RequestParam String diseaseType
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getDoctorsTotalExamCount(userLogin, diseaseType));
    }

    @GetMapping("/patient_total_exam_count")
    @RolesAllowed("PATIENT")
    public ResponseEntity<Long> getPatientsTotalExamCount(
            @RequestHeader (name="Authorization") String token,
            @RequestParam String departmentType
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getPatientsTotalExamCount(userLogin, departmentType));
    }

    @PostMapping("/doctor")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<CustomPage<MedicalExamResponse>> getDoctorsMedicalExams(
            @RequestHeader (name="Authorization") String token,
            @RequestParam int pageIndex,
            @RequestParam int pageSize,
            @RequestBody StringRequest birthNumber,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String doctor,
            @RequestParam(required = false) String department
            ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        String doctorDepartment = (String) jwtService.extractClaim(token.substring(7), "department");
        boolean isGeneralPractitioner = doctorDepartment.equalsIgnoreCase(GlobalConstants.GENERAL_PRACTITIONERS_CLINIC);
        Pageable page = PageRequest.of(pageIndex, pageSize);
        return ResponseEntity.ok(service.getDoctorsExams(userLogin, birthNumber.getValue(), isGeneralPractitioner,
                type, doctor, department, page));
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RolesAllowed("DOCTOR")
    public ResponseEntity<String> createMedExam(
            @RequestHeader(name="Authorization") String token,
            @ModelAttribute MedicalExamRequest request
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.createMedicalExam(userLogin, request));
    }
}
