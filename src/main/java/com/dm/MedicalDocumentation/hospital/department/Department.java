package com.dm.MedicalDocumentation.hospital.department;

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
public class Department {
    @EmbeddedId
    private DepartmentID id;
}
