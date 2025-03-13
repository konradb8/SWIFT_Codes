package io.github.konradb8.swift.swiftservice.service;


import io.github.konradb8.swift.swiftservice.model.SwiftCode;
import io.github.konradb8.swift.swiftservice.repository.SwiftCodeRepository;
import org.apache.commons.csv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVImportService {

    private final SwiftCodeRepository swiftCodeRepository;

    public CSVImportService(SwiftCodeRepository swiftCodeRepository) {
        this.swiftCodeRepository = swiftCodeRepository;
    }

    public void importCsv(MultipartFile file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(',')
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim());

        List<SwiftCode> swiftCodes = new ArrayList<>();

        for (CSVRecord record : csvParser) {
            SwiftCode swiftCode = new SwiftCode();
            swiftCode.setCountryISO2(record.get("COUNTRY ISO2 CODE"));
            swiftCode.setSwiftCode(record.get("SWIFT CODE"));
            swiftCode.setCodeType(record.get("CODE TYPE"));
            swiftCode.setName(record.get("NAME"));
            swiftCode.setAddress(record.get("ADDRESS"));
            swiftCode.setTownName(record.get("TOWN NAME"));
            swiftCode.setCountryName(record.get("COUNTRY NAME"));
            swiftCode.setTimeZone(record.get("TIME ZONE"));

            swiftCodes.add(swiftCode);
        }

        swiftCodeRepository.saveAll(swiftCodes);
    }
}