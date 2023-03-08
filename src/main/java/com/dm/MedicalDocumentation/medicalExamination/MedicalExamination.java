package com.dm.MedicalDocumentation.medicalExamination;

import com.dm.MedicalDocumentation.attachment.Attachment;
import com.dm.MedicalDocumentation.disease.Disease;
import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentType;
import com.dm.MedicalDocumentation.medicalExamination.type.ExaminationType;
import com.dm.MedicalDocumentation.patient.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class MedicalExamination {
    @Id
    @GeneratedValue
    private Long medicalExaminationId;
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
    @ManyToOne
    @JoinColumn(name = "department_type", nullable = false)
    private DepartmentType departmentType;
    @Column(nullable = false)
    private LocalDateTime startTime;
    @Column(nullable = false)
    private LocalDateTime endTime;
    @OneToMany(cascade = { CascadeType.ALL },mappedBy = "attachmentId", fetch = FetchType.LAZY)
    private List<Attachment> attachments;
}
