package com.proovitoo.entity;

import io.micronaut.data.annotation.*;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.UUID;


@MappedEntity("bike_part")
@Serdeable
@Getter
@Setter
@NoArgsConstructor
@ToString
public class BikePart {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    private String name;

    private String brand;
    private String category;
    private int quantity = 1;
    private String location;
    private String notes;

    @DateCreated
    private Instant createdAt;

    @DateUpdated
    private Instant updatedAt;
}
