package com.dm.MedicalDocumentation.disease;

import com.dm.MedicalDocumentation.disease.type.DiseaseType;
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
public class Disease {
    @Id
    @GeneratedValue
    private Long diseaseId;
    @ManyToOne
    @JoinColumn(name = "disease_type_id", nullable = false)
    private DiseaseType diseaseType;
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    @Column(nullable = false)
    private LocalDateTime diagnosed;
    private LocalDateTime cured;
}
