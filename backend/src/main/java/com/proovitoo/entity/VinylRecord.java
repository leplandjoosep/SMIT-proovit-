package com.proovitoo.entity;

import io.micronaut.data.annotation.*;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@MappedEntity("vinyl_record")
@Serdeable
@Getter
@Setter
@NoArgsConstructor
@ToString
public class VinylRecord {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    private String title;

    @NotBlank
    private String artist;

    private Integer releaseYear;
    private String acquiredFrom;
    private LocalDate acquiredDate;
    private String location;
    private String notes;

    private String fingerprint;

    @DateCreated
    private Instant createdAt;

    @DateUpdated
    private Instant updatedAt;
}
