package com.proovitoo.controller;

import com.proovitoo.DTO.BikePartDTO;
import com.proovitoo.DTO.LoanDTO;
import com.proovitoo.entity.BikePart;
import com.proovitoo.entity.Loan;
import com.proovitoo.service.BikePartService;
import com.proovitoo.service.LoanService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class LoanControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    LoanService loanService;

    @Inject
    BikePartService bikePartService;

    private BikePart testPart;
    private Loan testLoan;

    @BeforeEach
    void setUp() {
        loanService.getAllLoans().forEach(loan -> {
            if (loan.getReturnedAt() == null) {
                loanService.returnLoan(loan.getId());
            }
        });
        bikePartService.getAllParts().forEach(part -> bikePartService.delete(part.getId()));
        
        BikePartDTO partDto = BikePartDTO.builder()
                .name("Test Tire")
                .brand("Maxxis")
                .quantity(2)
                .build();
        
        testPart = bikePartService.addPart(partDto);
        
        LoanDTO loanDto = LoanDTO.builder()
                .bikePartId(testPart.getId())
                .borrowerName("Test Borrower")
                .dueAt(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();
        
        testLoan = loanService.addLoan(loanDto);
    }

    @Test
    void testGetAllLoans() {
        var request = HttpRequest.GET("/api/loan");
        var response = client.toBlocking().exchange(request, LoanDTO[].class);
        
        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getBody().orElse(null));
    }

    @Test
    void testAddLoan() {
        LoanDTO newLoan = LoanDTO.builder()
                .bikePartId(testPart.getId())
                .borrowerName("New Borrower")
                .build();

        var request = HttpRequest.POST("/api/loan", newLoan);
        var response = client.toBlocking().exchange(request, LoanDTO.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertNotNull(response.getBody().orElse(null));
        assertEquals("New Borrower", response.getBody().get().borrowerName());
    }

    @Test
    void testReturnLoan() {
        var request = HttpRequest.POST("/api/loan/" + testLoan.getId() + "/return", "");
        var response = client.toBlocking().exchange(request, LoanDTO.class);
        
        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getBody().orElse(null));
        assertNotNull(response.getBody().get().returnedAt());
    }
}
