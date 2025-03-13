package io.github.konradb8.swift.swiftservice.controllerTests;

import io.github.konradb8.swift.swiftservice.model.SwiftCode;
import io.github.konradb8.swift.swiftservice.repository.SwiftCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class SwiftCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SwiftCodeRepository swiftCodeRepository;

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

//    @Test
//    void testImportCsv() throws Exception{
//
//
//    }

}

