package com.dm.MedicalDocumentation.hospital;

import com.dm.MedicalDocumentation.hospital.department.Department;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Hospital {
    @Id
    @GeneratedValue
    private Long hospitalId;

    @Column(nullable = false, length = 70)
    private String hospitalName;

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "id.hospital", fetch = FetchType.LAZY)
    private List<Department> departments;
}
