package com.proovitoo.controller;

import com.proovitoo.DTO.VinylRecordDTO;
import com.proovitoo.entity.VinylRecord;
import com.proovitoo.service.VinylRecordService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class VinylRecordControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    VinylRecordService vinylRecordService;

    private VinylRecord testVinyl;

    @BeforeEach
    void setUp() {
        vinylRecordService.getAllVinyls().forEach(vinyl -> vinylRecordService.delete(vinyl.getId()));
        
        VinylRecordDTO dto = VinylRecordDTO.builder()
                .title("Test Album")
                .artist("Test Artist")
                .releaseYear(2020)
                .acquiredFrom("Test Store")
                .acquiredDate(LocalDate.of(2023, 1, 15))
                .build();
        
        testVinyl = vinylRecordService.addVinyl(dto);
    }

    @Test
    void testGetAllVinyls() {
        var request = HttpRequest.GET("/api/vinyl");
        var response = client.toBlocking().exchange(request, VinylRecordDTO[].class);
        
        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getBody().orElse(null));
    }

    @Test
    void testAddVinyl() {
        VinylRecordDTO newVinyl = VinylRecordDTO.builder()
                .title("New Album")
                .artist("New Artist")
                .releaseYear(2023)
                .build();

        var request = HttpRequest.POST("/api/vinyl", newVinyl);
        var response = client.toBlocking().exchange(request, VinylRecordDTO.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertNotNull(response.getBody().orElse(null));
        assertEquals("New Album", response.getBody().get().title());
    }

    @Test
    void testSearchVinyls() {
        var request = HttpRequest.GET("/api/vinyl/search?q=Test&page=0&size=10");
        var response = client.toBlocking().exchange(request, String.class);
        
        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getBody().orElse(null));
    }

    @Test
    void testDeleteVinyl() {
        var request = HttpRequest.DELETE("/api/vinyl/" + testVinyl.getId());
        var response = client.toBlocking().exchange(request);
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }
}
