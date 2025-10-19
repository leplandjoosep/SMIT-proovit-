package com.proovitoo.service;

import com.proovitoo.DTO.BikePartDTO;
import com.proovitoo.DTO.LoanDTO;
import com.proovitoo.entity.BikePart;
import com.proovitoo.entity.Loan;
import com.proovitoo.repository.BikePartRepo;
import com.proovitoo.repository.LoanRepo;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class LoanServiceTest {

    @Inject
    LoanService loanService;

    @Inject
    LoanRepo loanRepo;

    @Inject
    BikePartService bikePartService;

    @Inject
    BikePartRepo bikePartRepo;

    private BikePart testPart;

    @BeforeEach
    void setUp() {
        loanRepo.deleteAll();
        bikePartRepo.deleteAll();
        
        BikePartDTO partDto = BikePartDTO.builder()
                .name("Test Tire")
                .brand("Maxxis")
                .quantity(2)
                .build();
        
        testPart = bikePartService.addPart(partDto);
    }

    @Test
    void testAddAndReturnLoan() {
        LoanDTO dto = LoanDTO.builder()
                .bikePartId(testPart.getId())
                .borrowerName("Test Borrower")
                .build();

        Loan saved = loanService.addLoan(dto);
        assertNotNull(saved.getId());
        assertEquals("Test Borrower", saved.getBorrowerName());
        assertNull(saved.getReturnedAt());

        Loan returned = loanService.returnLoan(saved.getId());
        assertNotNull(returned.getReturnedAt());
    }

    @Test
    void testLoanQuantityLimit() {
        BikePartDTO partDto = BikePartDTO.builder()
                .name("Limited Part")
                .quantity(1)
                .build();
        
        BikePart limitedPart = bikePartService.addPart(partDto);
        
        LoanDTO firstLoan = LoanDTO.builder()
                .bikePartId(limitedPart.getId())
                .borrowerName("First Borrower")
                .build();
        
        Loan loan1 = loanService.addLoan(firstLoan);
        assertNotNull(loan1);
        
        LoanDTO secondLoan = LoanDTO.builder()
                .bikePartId(limitedPart.getId())
                .borrowerName("Second Borrower")
                .build();
        
        assertThrows(HttpStatusException.class, () -> loanService.addLoan(secondLoan));
    }

    @Test
    void testGetLoansByPart() {
        LoanDTO dto = LoanDTO.builder()
                .bikePartId(testPart.getId())
                .borrowerName("Test Borrower")
                .build();

        loanService.addLoan(dto);
        
        List<Loan> loans = loanService.getLoansByPart(testPart.getId(), true);
        assertEquals(1, loans.size());
        assertEquals("Test Borrower", loans.get(0).getBorrowerName());
    }
}
