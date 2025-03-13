package io.github.konradb8.swift.swiftservice.serviceTest;

import io.github.konradb8.swift.swiftservice.model.SwiftCode;
import io.github.konradb8.swift.swiftservice.model.SwiftCodeRequest;
import io.github.konradb8.swift.swiftservice.model.SwiftCodeResponse;
import io.github.konradb8.swift.swiftservice.model.SwiftCodesByCountryResponse;
import io.github.konradb8.swift.swiftservice.repository.SwiftCodeRepository;
import io.github.konradb8.swift.swiftservice.service.SwiftCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SwiftCodeServiceTest {

    @Mock
    private SwiftCodeRepository repository;

    @Spy
    @InjectMocks
    private SwiftCodeService service;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        service = spy(new SwiftCodeService(repository));

    }
    @Test
    void testFindBySwiftCode_found(){

        SwiftCode swiftCode = new SwiftCode();
        swiftCode.setSwiftCode("HD837KR2XXX");
        swiftCode.setIsHeadquarter(true);
        when(repository.findBySwiftCode("HD837KR2XXX")).thenReturn(Optional.of(swiftCode));

        SwiftCodeResponse result = service.getSwiftCodeDetails("HD837KR2XXX");

        assertEquals("HD837KR2XXX", result.getSwiftCode());
        assertNotNull(result);
        assertTrue(result.isHeadquarter());
        verify(repository, times(1)).findBySwiftCode("HD837KR2XXX");
    }

    @Test
    void testFindBySwiftCode_notFound(){
        when(repository.findBySwiftCode("HD837KR2XXX")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.getSwiftCodeDetails("HD837KR2XXX")
        );

        assertEquals("404 NOT_FOUND \"SWIFT code not found\"", exception.getMessage());

        verify(repository, times(1)).findBySwiftCode("HD837KR2XXX");
    }

    @Test
    void FindByCountryCodeTest(){
        List<SwiftCode> swiftCodes = new ArrayList<>();
        swiftCodes.add(new SwiftCode("PL123456XXX", "Bank Polska", "PL", true));
        swiftCodes.add(new SwiftCode("PL654321AKS", "Bank Warszawa", "PL",false));

        when(repository.findByCountryISO2("PL")).thenReturn(swiftCodes);

        SwiftCodesByCountryResponse result = service.getSwiftCodesByCountry("PL");

        assertEquals(swiftCodes.size(), result.getSwiftCodes().size());

        for (int i = 0; i < swiftCodes.size(); i++) {
            assertEquals(swiftCodes.get(i).getSwiftCode(), result.getSwiftCodes().get(i).getSwiftCode());
            assertEquals(swiftCodes.get(i).getName(), result.getSwiftCodes().get(i).getBankName());
        }

        verify(repository, times(1)).findByCountryISO2("PL");
    }

    @Test
    void AddSwiftCodeTest(){
        SwiftCodeRequest swiftCodeRequest = new SwiftCodeRequest();
        swiftCodeRequest.setSwiftCode("PL123456XXX");
        swiftCodeRequest.setBankName("Bank Polska");
        swiftCodeRequest.setCountryISO2("PL");
        swiftCodeRequest.setCountryName("Poland");
        swiftCodeRequest.setHeadquarter(true);

        doReturn(true).when(service).isHeadquarter(swiftCodeRequest.getSwiftCode());

        SwiftCode swiftCode = new SwiftCode();
        swiftCode.setSwiftCode(swiftCodeRequest.getSwiftCode());
        swiftCode.setName(swiftCodeRequest.getBankName());
        swiftCode.setCountryISO2(swiftCodeRequest.getCountryISO2());
        swiftCode.setIsHeadquarter(swiftCodeRequest.isHeadquarter());

        when(repository.save(any(SwiftCode.class))).thenReturn(swiftCode);

        SwiftCode result = service.addSwiftCode(swiftCodeRequest);

        assertNotNull(result);
        assertEquals(swiftCode.getSwiftCode(), result.getSwiftCode());
        assertEquals(swiftCode.getName(), result.getName());
        assertEquals(swiftCode.getCountryISO2(), result.getCountryISO2());
        assertEquals(swiftCode.getIsHeadquarter(), result.getIsHeadquarter());

        ArgumentCaptor<SwiftCode> captor = ArgumentCaptor.forClass(SwiftCode.class);
        verify(repository).save(captor.capture());

        SwiftCode capturedSwiftCode = captor.getValue();
        assertEquals("PL123456XXX", capturedSwiftCode.getSwiftCode());
        assertEquals("Bank Polska", capturedSwiftCode.getName());
        assertEquals("PL", capturedSwiftCode.getCountryISO2());
        assertTrue(capturedSwiftCode.getIsHeadquarter());

    }

    @Test
    void DeleteSwiftCodeTest(){

        SwiftCode swiftCode = new SwiftCode();
        swiftCode.setSwiftCode("PL123456XXX");

        when(repository.findBySwiftCode("PL123456XXX")).thenReturn(Optional.of(swiftCode));
        doNothing().when(repository).delete(swiftCode);

        service.deleteSwiftCode("PL123456XXX");

        verify(repository, times(1)).delete(swiftCode);
    }
}

