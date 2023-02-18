package com.dm.MedicalDocumentation.medicalExamination.type;

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
public class ExaminationType {
    @Id
    @GeneratedValue
    private Integer examinationTypeId;
    @Column(nullable = false, length = 50)
    private String examinationTypeName;
}
