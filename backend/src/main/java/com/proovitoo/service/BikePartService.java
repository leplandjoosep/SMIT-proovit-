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

    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Bike Part not found");
        }
        repo.deleteById(id);
    }

    public BikePart update(UUID id, BikePartDTO dto) {
        BikePart p = getPartById(id);
        p.setName(dto.name());
        p.setBrand(dto.brand());
        p.setCategory(dto.category());
        p.setLocation(dto.location());
        p.setNotes(dto.notes());

        int q = dto.quantity();
        if (q <= 0) {
            q = 1;
        }
        p.setQuantity(q);

        return repo.update(p);
    }
}
