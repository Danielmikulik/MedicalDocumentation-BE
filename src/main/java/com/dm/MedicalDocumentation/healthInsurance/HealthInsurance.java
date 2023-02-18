package com.dm.MedicalDocumentation.healthInsurance;

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
public class HealthInsurance {
    @Id
    @GeneratedValue
    private Integer insuranceId;
    @Column(nullable = false, length = 50)
    private String insuranceName;
}
