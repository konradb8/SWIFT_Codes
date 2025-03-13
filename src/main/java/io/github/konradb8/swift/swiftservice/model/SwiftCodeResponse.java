package io.github.konradb8.swift.swiftservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonPropertyOrder({"address", "bankName", "countryISO2", "countryName", "isHeadquarter", "swiftCode", "branches"})
public class SwiftCodeResponse {
    private String address;
    private String bankName;
    private String countryISO2;
    private String countryName;
    private String swiftCode;
    @JsonProperty("isHeadquarter")
    private boolean headquarter;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<SwiftCodeResponse> branches;

    public SwiftCodeResponse(SwiftCode swiftCode) {
        this.address = swiftCode.getAddress();
        this.bankName = swiftCode.getName();
        this.countryISO2 = swiftCode.getCountryISO2();
        this.countryName = swiftCode.getCountryName();
        this.swiftCode = swiftCode.getSwiftCode();
        this.branches = new ArrayList<>();
    }

    public static SwiftCodeResponse fromEntity(SwiftCode entity) {
        return new SwiftCodeResponse(entity);
    }

}