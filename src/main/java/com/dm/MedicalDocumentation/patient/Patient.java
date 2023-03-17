package com.dm.MedicalDocumentation.patient;

import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.healthInsurance.HealthInsurance;
import com.dm.MedicalDocumentation.person.Person;
import com.dm.MedicalDocumentation.user.User;
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
public class Patient {
    @Id
    @GeneratedValue
    private Long patientId;
    @OneToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
    @ManyToOne
    @JoinColumn(name = "general_practitioner")
    private Doctor generalPractitioner;
    @OneToOne
    @JoinColumn(name = "insurance_id", nullable = false)
    private HealthInsurance healthInsurance;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
