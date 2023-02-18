package com.dm.MedicalDocumentation.doctor.history;

import com.dm.MedicalDocumentation.doctor.Doctor;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Embeddable
public class DoctorHistoryID implements Serializable {
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    private LocalDate dateFrom;
}
