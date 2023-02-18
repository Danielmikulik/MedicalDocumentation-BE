package com.dm.MedicalDocumentation.hospital.department;

import com.dm.MedicalDocumentation.hospital.Hospital;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
