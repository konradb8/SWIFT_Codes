package io.github.konradb8.swift.swiftservice.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "swift_codes")
public class SwiftCode {

    @Column(name = "COUNTRY ISO2 CODE")
    private String countryISO2;

    @Id
    @Column(name = "SWIFT CODE")
    private String swiftCode;

    @Column(name = "CODE TYPE")
    private String codeType;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "TOWN NAME")
    private String townName;

    @Column(name = "COUNTRY NAME")
    private String countryName;

    @Column(name = "TIME ZONE")
    private String timeZone;

    @Transient
    private Boolean isHeadquarter;

    public SwiftCode(String swiftCode, String bankName, String ISO2, boolean isHeadquarter) {
        this.swiftCode = swiftCode;
        this.name = bankName;
        this.countryISO2 = ISO2;
        this.isHeadquarter = isHeadquarter;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SwiftCode swiftCode1 = (SwiftCode) o;
        return Objects.equals(countryISO2, swiftCode1.countryISO2) && Objects.equals(swiftCode, swiftCode1.swiftCode) && Objects.equals(codeType, swiftCode1.codeType) && Objects.equals(name, swiftCode1.name) && Objects.equals(address, swiftCode1.address) && Objects.equals(townName, swiftCode1.townName) && Objects.equals(countryName, swiftCode1.countryName) && Objects.equals(timeZone, swiftCode1.timeZone) && Objects.equals(isHeadquarter, swiftCode1.isHeadquarter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryISO2, swiftCode, codeType, name, address, townName, countryName, timeZone, isHeadquarter);
    }
}