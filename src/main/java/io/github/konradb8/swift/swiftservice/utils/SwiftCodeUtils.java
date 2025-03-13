package io.github.konradb8.swift.swiftservice.utils;

public class SwiftCodeUtils {
    public static boolean isHeadquarter(String swiftCode) {
        return swiftCode.endsWith("XXX");
    }
}