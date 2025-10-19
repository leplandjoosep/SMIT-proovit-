package com.proovitoo.service;

import com.proovitoo.DTO.VinylRecordDTO;
import com.proovitoo.entity.VinylRecord;
import com.proovitoo.repository.VinylRecordRepo;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class VinylRecordServiceTest {

    @Inject
    VinylRecordService vinylRecordService;

    @Inject
    VinylRecordRepo vinylRecordRepo;

    @BeforeEach
    void setUp() {
        vinylRecordRepo.deleteAll();
    }

    @Test
    void testAddAndGetVinyl() {
        VinylRecordDTO dto = VinylRecordDTO.builder()
                .title("Test Album")
                .artist("Test Artist")
                .releaseYear(2020)
                .acquiredFrom("Test Store")
                .acquiredDate(LocalDate.of(2023, 1, 15))
                .build();

        VinylRecord saved = vinylRecordService.addVinyl(dto);
        assertNotNull(saved.getId());
        assertEquals("Test Album", saved.getTitle());

        VinylRecord retrieved = vinylRecordService.getById(saved.getId());
        assertEquals(saved.getId(), retrieved.getId());
        assertEquals("Test Album", retrieved.getTitle());
    }

    @Test
    void testSearchVinyls() {
        vinylRecordService.addVinyl(VinylRecordDTO.builder().title("Dark Side").artist("Pink Floyd").build());
        vinylRecordService.addVinyl(VinylRecordDTO.builder().title("Abbey Road").artist("Beatles").build());

        var result = vinylRecordService.search("Dark", null, io.micronaut.data.model.Pageable.from(0, 10));
        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().get(0).getTitle().contains("Dark"));
    }

    @Test
    void testDeleteVinyl() {
        VinylRecordDTO dto = VinylRecordDTO.builder().title("Test Album").artist("Test Artist").build();
        VinylRecord saved = vinylRecordService.addVinyl(dto);
        
        vinylRecordService.delete(saved.getId());
        
        assertThrows(HttpStatusException.class, () -> 
            vinylRecordService.getById(saved.getId()));
    }

    @Test
    void testVinylNotFound() {
        assertThrows(HttpStatusException.class, () -> 
            vinylRecordService.getById(UUID.randomUUID()));
    }
}
