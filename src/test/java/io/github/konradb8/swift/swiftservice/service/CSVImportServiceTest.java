package io.github.konradb8.swift.swiftservice.service;

import io.github.konradb8.swift.swiftservice.model.SwiftCode;
import io.github.konradb8.swift.swiftservice.repository.SwiftCodeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVImportServiceTest {

    @Mock
    private SwiftCodeRepository swiftCodeRepository = Mockito.mock(SwiftCodeRepository.class);

    @Test
    public void testCSVImportServiceLoads() throws IOException {
        Resource fileResource = new ClassPathResource("csv-small.csv");
        assertNotNull(fileResource);

        MockMultipartFile file = new MockMultipartFile(
                "file",fileResource.getFilename(),
                MediaType.MULTIPART_FORM_DATA_VALUE,
                fileResource.getInputStream());

        CSVImportService csvImportService = new CSVImportService(swiftCodeRepository);
        assertDoesNotThrow(() -> csvImportService.importCsv(file));

        Mockito.verify(swiftCodeRepository, Mockito.times(1)).saveAll(
                getSwiftCodeFromCSV()
        );
    }

    private List<SwiftCode> getSwiftCodeFromCSV() {
        var swiftCode = new SwiftCode();
        swiftCode.setSwiftCode("ABIEBGS1XXX");
        swiftCode.setCountryISO2("BG");
        swiftCode.setCodeType("BIC11");
        swiftCode.setName("ABV INVESTMENTS LTD");
        swiftCode.setAddress("TSAR ASEN 20  VARNA, VARNA, 9002");
        swiftCode.setTownName("VARNA");
        swiftCode.setCountryName("BULGARIA");
        swiftCode.setTimeZone("Europe/Sofia");

        var list = new ArrayList<SwiftCode>();
        list.add(swiftCode);

        return list;
    }
}