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
}