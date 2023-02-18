package com.dm.MedicalDocumentation.doctor;

import com.dm.MedicalDocumentation.hospital.department.Department;
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
public class Doctor {
    @Id
    @GeneratedValue
    private Long doctorId;
    @OneToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "department", referencedColumnName = "department_type", nullable = false),
            @JoinColumn(name = "hospital", referencedColumnName = "hospital", nullable = false)
    })
    private Department department;
}
