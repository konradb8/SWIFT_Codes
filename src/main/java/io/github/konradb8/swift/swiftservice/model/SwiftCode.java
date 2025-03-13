package io.github.konradb8.swift.swiftservice.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "swift_codes")
public class SwiftCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "COUNTRY ISO2 CODE")
    private String countryISO2;

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

    @Column(name = "IS HEADQUARTER")
    private Boolean isHeadquarter;

    public SwiftCode(String swiftCode, String bankName, String ISO2, boolean isHeadquarter) {
        this.swiftCode = swiftCode;
        this.name = bankName;
        this.countryISO2 = ISO2;
        this.isHeadquarter = isHeadquarter;
    }
}
