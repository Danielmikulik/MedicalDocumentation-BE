package com.dm.MedicalDocumentation.prescription;

import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.medication.Medication;
import com.dm.MedicalDocumentation.patient.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Prescription {
    @Id
    @GeneratedValue
    private Integer prescriptionId;
    @ManyToOne
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    @Column(nullable = false)
    private Integer amount;
    @Column(nullable = false)
    private Boolean retrieved;

}
