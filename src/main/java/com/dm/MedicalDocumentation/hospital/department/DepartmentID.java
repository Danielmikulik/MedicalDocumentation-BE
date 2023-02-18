package com.dm.MedicalDocumentation.hospital.department;

import com.dm.MedicalDocumentation.hospital.Hospital;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
public class DepartmentID implements Serializable {

    @ManyToOne
    @JoinColumn(name = "department_type")
    private DepartmentType departmentType;
    @ManyToOne
    @JoinColumn(name = "hospital")
    private Hospital hospital;

}
