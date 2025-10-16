package com.proovitoo.DTO;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Serdeable
@Builder
public record LoanDTO(
        UUID id,
        UUID bikePartId,
        String borrowerName,
        Instant borrowedAt,
        Instant dueAt,
        Instant returnedAt
) {}
