package com.proovitoo.DTO;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Serdeable
@Builder
public record VinylRecordDTO(UUID id,
                             String title,
                             String artist,
                             Integer releaseYear,
                             String acquiredFrom,
                             LocalDate acquiredDate,
                             String location,
                             String notes,
                             String fingerprint,
                             Instant createdAt,
                             Instant updatedAt) {
}
