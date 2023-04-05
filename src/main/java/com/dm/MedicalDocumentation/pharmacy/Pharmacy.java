package com.dm.MedicalDocumentation.pharmacy;

import com.dm.MedicalDocumentation.city.City;
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
public class Pharmacy {
    @Id
    @GeneratedValue
    private Long pharmacyId;
    @Column(nullable = false, length = 70)
    private String pharmacyName;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "zip_code", nullable = false)
    private City city;
    @Column(nullable = false)
    private String address;
}
