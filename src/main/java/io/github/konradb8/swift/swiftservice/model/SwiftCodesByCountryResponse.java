package io.github.konradb8.swift.swiftservice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SwiftCodesByCountryResponse {
    private String countryISO2;
    private String countryName;
    private List<SwiftCodeShortResponse> swiftCodes;

    public SwiftCodesByCountryResponse(String countryISO2, String countryName) {
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
        this.swiftCodes = new ArrayList<>();
    }

}
