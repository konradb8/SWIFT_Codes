package io.github.konradb8.swift.swiftservice.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@JsonPropertyOrder({"address", "bankName", "countryISO2", "isHeadquarter", "swiftCode", "branches"})
public class SwiftCodeShortResponse {
    private String address;
    private String bankName;
    private String countryISO2;
    private String swiftCode;
    @JsonProperty("isHeadquarter")
    private Boolean headquarter;

    public SwiftCodeShortResponse(SwiftCode swiftCode) {
        this.address = swiftCode.getAddress();
        this.bankName = swiftCode.getName();
        this.countryISO2 = swiftCode.getCountryISO2();
        this.headquarter = swiftCode.getIsHeadquarter();
        this.swiftCode = swiftCode.getSwiftCode();
    }

    public static SwiftCodeShortResponse fromEntity(SwiftCode entity) {
        return new SwiftCodeShortResponse(entity);
    }

}