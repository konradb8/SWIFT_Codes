package io.github.konradb8.swift.swiftservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SwiftApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwiftApplication.class, args);

        System.out.println("Hello World");
    }
}
