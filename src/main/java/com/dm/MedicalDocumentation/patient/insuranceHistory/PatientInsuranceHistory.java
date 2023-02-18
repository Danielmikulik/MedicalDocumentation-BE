package com.dm.MedicalDocumentation.patient.insuranceHistory;

import com.dm.MedicalDocumentation.healthInsurance.HealthInsurance;
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
public class PatientInsuranceHistory {
    @EmbeddedId
    private PatientInsuranceHistoryID id;
    private LocalDate dateTo;
    @ManyToOne
    @JoinColumn(name = "insurance_id", nullable = false)
    private HealthInsurance insurance;
}
