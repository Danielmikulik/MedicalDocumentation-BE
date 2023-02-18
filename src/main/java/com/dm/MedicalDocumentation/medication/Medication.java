package com.dm.MedicalDocumentation.medication;

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
public class Medication {
    @Id
    @GeneratedValue
    private Integer medicationId;
    @Column(nullable = false, length = 50)
    private String medicationName;
    private Integer amount;
    @Column(length = 10)
    private String unit;
}
