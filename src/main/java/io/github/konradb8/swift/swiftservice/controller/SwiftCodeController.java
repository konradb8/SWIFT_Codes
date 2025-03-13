package io.github.konradb8.swift.swiftservice.controller;

import io.github.konradb8.swift.swiftservice.model.SwiftCodeRequest;
import io.github.konradb8.swift.swiftservice.service.CSVImportService;
import io.github.konradb8.swift.swiftservice.service.SwiftCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/swift-codes")
public class SwiftCodeController {

    private final SwiftCodeService swiftCodeService;

    private final CSVImportService csvImportService;

    public SwiftCodeController(SwiftCodeService swiftCodeService, CSVImportService csvImportService) {
        this.swiftCodeService = swiftCodeService;
        this.csvImportService = csvImportService;
    }

    @PostMapping(value = "/import-csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file) {
        try {
            csvImportService.importCsv(file);
            return ResponseEntity.ok("CSV file imported successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error importing CSV file: " + e.getMessage());
        }
    }

    // Endpoint 1: return swift code details
    @GetMapping("/{swiftCode}")
    public ResponseEntity<?> getSwiftCodeDetails(@PathVariable String swiftCode) {
        return ResponseEntity.ok(swiftCodeService.getSwiftCodeDetails(swiftCode));
    }

    // Endpoint 2: return all SWIFT codes for a specific country
    @GetMapping(value = "/country/{countryISO2}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSwiftCodesByCountry(@PathVariable String countryISO2) {
        return ResponseEntity.ok(swiftCodeService.getSwiftCodesByCountry(countryISO2));
    }

    // Endpoint 3: adds new SWIFT code entries
    @PostMapping
    public ResponseEntity<?> addSwiftCode(@RequestBody SwiftCodeRequest request) {
        swiftCodeService.addSwiftCode(request);
        return ResponseEntity.ok(Collections.singletonMap("message", "SWIFT code added successfully"));
    }

    // Endpoint 4: deletes a SWIFT code
    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<?> deleteSwiftCode(@PathVariable String swiftCode) {
        swiftCodeService.deleteSwiftCode(swiftCode);
        return ResponseEntity.ok(Collections.singletonMap("message", "SWIFT code deleted successfully"));
    }


    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(ResponseStatusException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getReason());
        return errorResponse;
    }
}

