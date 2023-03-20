package com.dm.MedicalDocumentation.patient.insuranceHistory;

import com.dm.MedicalDocumentation.patient.Patient;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PatientInsuranceHistoryID implements Serializable {
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    private LocalDate dateFrom;
}
