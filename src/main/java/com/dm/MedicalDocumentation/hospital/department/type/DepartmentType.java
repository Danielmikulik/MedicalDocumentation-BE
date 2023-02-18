package com.dm.MedicalDocumentation.hospital.department.type;

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
public class DepartmentType {
    @Id
    @GeneratedValue
    private Integer departmentTypeId;
    @Column(nullable = false, length = 50)
    private String departmentTypeName;
}
