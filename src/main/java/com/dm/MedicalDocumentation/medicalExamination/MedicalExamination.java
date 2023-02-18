package com.dm.MedicalDocumentation.medicalExamination;

import com.dm.MedicalDocumentation.disease.Disease;
import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.medicalExamination.type.ExaminationType;
import com.dm.MedicalDocumentation.patient.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class MedicalExamination {
    @Id
    @GeneratedValue
    private Integer medicalExaminationId;
    @ManyToOne
    @JoinColumn(name = "examination_type_id", nullable = false)
    private ExaminationType type;
    @ManyToOne
    @JoinColumn(name = "disease_id")
    private Disease disease;
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    @Column(nullable = false)
    private LocalDateTime startTime;
    @Column(nullable = false)
    private LocalDateTime endTime;
}
