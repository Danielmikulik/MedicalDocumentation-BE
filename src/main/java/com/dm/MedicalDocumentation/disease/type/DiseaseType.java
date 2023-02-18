package com.dm.MedicalDocumentation.disease.type;

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
public class DiseaseType {
    @Id
    @GeneratedValue
    private Integer diseaseTypeId;
    @Column(nullable = false, length = 50)
    private String diseaseTypeName;
}
