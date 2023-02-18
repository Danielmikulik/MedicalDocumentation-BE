package com.dm.MedicalDocumentation.doctor.history;

import com.dm.MedicalDocumentation.healthInsurance.HealthInsurance;
import com.dm.MedicalDocumentation.hospital.department.Department;
import com.dm.MedicalDocumentation.patient.insuranceHistory.PatientInsuranceHistoryID;
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
public class DoctorHistory {
    @EmbeddedId
    private DoctorHistoryID id;
    private LocalDate dateTo;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "department_type", referencedColumnName = "department_type", nullable = false),
            @JoinColumn(name = "hospital", referencedColumnName = "hospital", nullable = false)
    })
    private Department department;
}
