package com.proovitoo.entity;

import io.micronaut.data.annotation.*;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@MappedEntity("loan")
@Serdeable
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Loan {

    @Id
    @GeneratedValue
    private UUID id;

    @Relation(Relation.Kind.MANY_TO_ONE)
    @MappedProperty("bike_part_id")
    private BikePart bikePart;

    @NotBlank
    private String borrowerName;

    private Instant borrowedAt;
    private Instant dueAt;
    private Instant returnedAt;
}
