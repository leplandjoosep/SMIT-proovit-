package com.proovitoo.service;

import com.proovitoo.DTO.BikePartDTO;
import com.proovitoo.entity.BikePart;
import com.proovitoo.repository.BikePartRepo;
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
class BikePartServiceTest {

    @Inject
    BikePartService bikePartService;

    @Inject
    BikePartRepo bikePartRepo;

    @BeforeEach
    void setUp() {
        bikePartRepo.deleteAll();
    }

    @Test
    void testAddAndGetPart() {
        BikePartDTO dto = BikePartDTO.builder()
                .name("Test Tire")
                .brand("Maxxis")
                .category("TIRE")
                .quantity(3)
                .build();

        BikePart saved = bikePartService.addPart(dto);
        assertNotNull(saved.getId());
        assertEquals("Test Tire", saved.getName());

        BikePart retrieved = bikePartService.getPartById(saved.getId());
        assertEquals(saved.getId(), retrieved.getId());
        assertEquals("Test Tire", retrieved.getName());
    }

    @Test
    void testSearchParts() {
        bikePartService.addPart(BikePartDTO.builder().name("Maxxis Tire").category("TIRE").build());
        bikePartService.addPart(BikePartDTO.builder().name("Shimano Brake").category("BRAKE").build());

        var result = bikePartService.search("Tire", null, io.micronaut.data.model.Pageable.from(0, 10));
        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().get(0).getName().contains("Tire"));
    }

    @Test
    void testDeletePart() {
        BikePartDTO dto = BikePartDTO.builder().name("Test Part").build();
        BikePart saved = bikePartService.addPart(dto);
        
        bikePartService.delete(saved.getId());
        
        assertThrows(HttpStatusException.class, () -> 
            bikePartService.getPartById(saved.getId()));
    }

    @Test
    void testPartNotFound() {
        assertThrows(HttpStatusException.class, () -> 
            bikePartService.getPartById(UUID.randomUUID()));
    }
}
