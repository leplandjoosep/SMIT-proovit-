package com.proovitoo.service;

import com.proovitoo.DTO.BikePartDTO;
import com.proovitoo.entity.BikePart;
import com.proovitoo.mapper.BikePartMapper;
import com.proovitoo.repository.BikePartRepo;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class BikePartService {

    private final BikePartRepo repo;
    private final BikePartMapper mapper;

    public BikePart getPartById(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, "Bike Part not found"));
    }

    public List<BikePart> getAllParts() {
        return repo.findAll();
    }

    public BikePart addPart(BikePartDTO bikePartDTO) {
        BikePart entity = mapper.toEntity(bikePartDTO);

        if (entity.getQuantity() <= 0) {
            entity.setQuantity(1);
        }

        return repo.save(entity);
    }
}
