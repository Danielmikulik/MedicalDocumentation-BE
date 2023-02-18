package com.dm.MedicalDocumentation.hospital;

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
public class Hospital {
    @Id
    @GeneratedValue
    private Integer hospitalId;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false, length = 70)
    private String hospitalName;
}
