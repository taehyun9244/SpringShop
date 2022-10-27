package com.example.mvcprac.model;

import com.example.mvcprac.dto.zone.ZoneForm;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"city", "province"}))
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String localNameOfCity;

    @Column(nullable = true)
    private String province;

    public Zone(String city, String localNameOfCity, String province) {
        this.city = city;
        this.localNameOfCity = localNameOfCity;
        this.province = province;
    }

    public Zone(ZoneForm zoneForm) {
        this.city = zoneForm.getCityName();
        this.localNameOfCity = zoneForm.getLocalNameOfCity();
        this.province = zoneForm.getProvinceName();
    }

    @Override
    public String toString() {
        return String.format("%s(%s)/%s", city, localNameOfCity, province);
    }
}
