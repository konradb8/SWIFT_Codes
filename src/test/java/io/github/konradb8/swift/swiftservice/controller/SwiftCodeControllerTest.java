package io.github.konradb8.swift.swiftservice.controller;

import io.github.konradb8.swift.swiftservice.model.SwiftCode;
import io.github.konradb8.swift.swiftservice.repository.SwiftCodeRepository;
import io.github.konradb8.swift.swiftservice.service.CSVImportService;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class SwiftCodeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SwiftCodeRepository swiftCodeRepository;

    @MockitoBean
    private CSVImportService csvImportService;

    @Test
    void testGetBySwiftCode_found() throws Exception {
        SwiftCode swiftCode = new SwiftCode();
        swiftCode.setSwiftCode("ABJ2783OXXX");
        swiftCode.setCountryISO2("PL");
        swiftCode.setName("Bank Testowy");
        swiftCode.setIsHeadquarter(true);
        swiftCodeRepository.save(swiftCode);

        mockMvc.perform(get("/v1/swift-codes/ABJ2783OXXX")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode").value("ABJ2783OXXX"));
    }

    @Test
    void testGetBySwiftCode_notFound() throws Exception {

        mockMvc.perform(get("/v1/swift-codes/INVALIDCODE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void testImportCsv_ok() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file","file.csv",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "Hello, World!".getBytes());

        doAnswer((Answer<Void>) invocation -> null)
                .when(csvImportService).importCsv(file);

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.POST, "/v1/swift-codes/import-csv")
                        .file(file)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void testImportCsv_error() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file","file.csv",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "Hello, World!".getBytes());

        doAnswer((Answer<Void>) invocation -> {
            throw new IOException();
        }).when(csvImportService).importCsv(file);

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.POST, "/v1/swift-codes/import-csv")
                        .file(file)
                )
                .andDo(print())
                .andExpect(status().is(500))
                .andReturn();
    }

    @Test
    void testGetSwiftCodesByCountry_found() throws Exception {
        SwiftCode swiftCode1 = new SwiftCode();
        swiftCode1.setSwiftCode("ABCDUYXXXX");
        swiftCode1.setCountryISO2("UY");
        swiftCode1.setName("Bank UY");
        swiftCode1.setIsHeadquarter(true);

        SwiftCode swiftCode2 = new SwiftCode();
        swiftCode2.setSwiftCode("XYZBUYXXXX");
        swiftCode2.setCountryISO2("UY");
        swiftCode2.setName("Bank XYZ");
        swiftCode2.setIsHeadquarter(false);

        swiftCodeRepository.saveAll(List.of(swiftCode1, swiftCode2));

        mockMvc.perform(get("/v1/swift-codes/country/UY")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryISO2").value("UY"))
                .andExpect(jsonPath("$.swiftCodes[0].swiftCode").value("ABCDUYXXXX"))
                .andExpect(jsonPath("$.swiftCodes[1].swiftCode").value("XYZBUYXXXX"));
    }

    @Test
    void testGetSwiftCodesByCountry_notFound() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/country/ZZ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddSwiftCode_ok() throws Exception {
        String requestJson = """
            {
                "swiftCode": "QWIS92R3XXX",
                "address": "1234 Toronto",
                "bankName": "Bank of Canada",
                "countryISO2": "CA",
                "countryName": "Canada"
            }
            """;

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("SWIFT code added successfully"));
    }

    @Test
    void testDeleteSwiftCode_ok() throws Exception {
        SwiftCode swiftCode = new SwiftCode();
        swiftCode.setSwiftCode("DELETE123XXX");
        swiftCode.setCountryISO2("DE");
        swiftCode.setName("Bank XXX");
        swiftCode.setIsHeadquarter(false);
        swiftCodeRepository.save(swiftCode);

        mockMvc.perform(delete("/v1/swift-codes/DELETE123XXX"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("SWIFT code deleted successfully"));
    }

    @Test
    void testDeleteSwiftCode_notFound() throws Exception {
        mockMvc.perform(delete("/v1/swift-codes/INVALIDCODE"))
                .andExpect(status().isNotFound());
    }



}