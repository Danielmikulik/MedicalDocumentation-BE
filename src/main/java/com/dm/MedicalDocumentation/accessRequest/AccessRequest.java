package com.dm.MedicalDocumentation.accessRequest;

import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.medicalExamination.MedicalExamination;
import com.dm.MedicalDocumentation.patient.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class AccessRequest {
    @Id
    @GeneratedValue
    private Integer requestId;
    @ManyToOne
    @JoinColumn(name = "medical_examination_id", nullable = false)
    private MedicalExamination medicalExamination;
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    @Column(nullable = false)
    private Boolean approved;
    @Column(nullable = false)
    private LocalDate accessibleUntil;
}
