package io.github.konradb8.swift.swiftservice.repository;

import io.github.konradb8.swift.swiftservice.model.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SwiftCodeRepository extends JpaRepository<SwiftCode, Long> {
    Optional<SwiftCode> findBySwiftCode(String swiftCode);
    List<SwiftCode> findByCountryISO2(String countryISO2);
    List<SwiftCode> findBySwiftCodeStartingWith(String swiftPrefix);
}