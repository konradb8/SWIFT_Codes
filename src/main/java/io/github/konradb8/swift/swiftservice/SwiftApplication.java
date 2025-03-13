package io.github.konradb8.swift.swiftservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Level;

@SpringBootApplication
public class SwiftApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwiftApplication.class, args);
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
    }
}