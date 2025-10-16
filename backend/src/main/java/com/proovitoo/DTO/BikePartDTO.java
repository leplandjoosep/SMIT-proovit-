package com.proovitoo.DTO;


import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Serdeable
@Builder
public record BikePartDTO(UUID id,
                          String name,
                          String brand,
                          String category,
                          int quantity,
                          String location,
                          String notes,
                          Instant createdAt,
                          Instant updatedAt
) {}
