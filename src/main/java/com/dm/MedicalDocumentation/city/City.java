package com.dm.MedicalDocumentation.city;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@Entity
@Table
public class City {

    @Id
    @Column(length = 6)
    private String zipCode;
    @Column(nullable = false)
    private String cityName;

    public City(String zipCode, String cityName) {
        this.zipCode = zipCode.replace(" ", "");
        this.cityName = cityName;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode.replace(" ", "");
    }
}
