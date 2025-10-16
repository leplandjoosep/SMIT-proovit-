package com.proovitoo.service;

import jakarta.inject.Singleton;
import com.proovitoo.DTO.LoanDTO;
import com.proovitoo.entity.BikePart;
import com.proovitoo.entity.Loan;
import com.proovitoo.mapper.LoanMapper;
import com.proovitoo.repository.BikePartRepo;
import com.proovitoo.repository.LoanRepo;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepo loanRepo;
    private final BikePartRepo partRepo;
    private final LoanMapper mapper;

    public Loan getLoanById(UUID id) {
        return loanRepo.findById(id)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, "Loan not found"));
    }

    public List<Loan> getAllLoans() {
        return loanRepo.findAll();
    }

    public Loan addLoan(LoanDTO dto) {
        if (dto.bikePartId() == null) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "bikePartId is required");
        }
        if (dto.borrowerName() == null || dto.borrowerName().isBlank()) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "borrowerName is required");
        }

        BikePart part = partRepo.findById(dto.bikePartId())
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, "Bike Part not found"));

        long out = loanRepo.countByBikePartIdAndReturnedAtIsNull(part.getId());
        int qty = (part.getQuantity() <= 0) ? 1 : part.getQuantity();
        if (out >= qty) {
            throw new HttpStatusException(HttpStatus.CONFLICT, "No available quantity to lend");
        }

        Loan loan = mapper.toEntity(dto);
        loan.setBikePart(part);
        loan.setBorrowedAt(Instant.now());

        return loanRepo.save(loan);
    }
}
