package com.dm.MedicalDocumentation.person;

import com.dm.MedicalDocumentation.city.City;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Person {
    @Id
    @GeneratedValue
    private Long personId;
    @Column(nullable = false, length = 20)
    private String name;
    @Column(nullable = false, length = 30)
    private String surname;
    @Column(length = 11, unique = true)
    private String birthNumber;
    @Column(nullable = false)
    private LocalDate birthDate;
    @OneToOne
    @JoinColumn(name = "zip_code", nullable = false)
    private City city;
    @Column(nullable = false)
    private String address;

    public String getFullName() {
        return name + " " + surname;
    }
}
