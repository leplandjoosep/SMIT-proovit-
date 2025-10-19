package com.proovitoo.controller;

import com.proovitoo.DTO.BikePartDTO;
import com.proovitoo.entity.BikePart;
import com.proovitoo.service.BikePartService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class BikePartControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    BikePartService bikePartService;

    private BikePart testPart;

    @BeforeEach
    void setUp() {
        bikePartService.getAllParts().forEach(part -> bikePartService.delete(part.getId()));
        
        BikePartDTO dto = BikePartDTO.builder()
                .name("Test Tire")
                .brand("Maxxis")
                .category("TIRE")
                .quantity(3)
                .location("Garage A1")
                .build();
        
        testPart = bikePartService.addPart(dto);
    }

    @Test
    void testGetAllParts() {
        var request = HttpRequest.GET("/api/part");
        var response = client.toBlocking().exchange(request, BikePartDTO[].class);
        
        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getBody().orElse(null));
    }

    @Test
    void testAddPart() {
        BikePartDTO newPart = BikePartDTO.builder()
                .name("New Tire")
                .brand("Continental")
                .category("TIRE")
                .quantity(2)
                .build();

        var request = HttpRequest.POST("/api/part", newPart);
        var response = client.toBlocking().exchange(request, BikePartDTO.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertNotNull(response.getBody().orElse(null));
        assertEquals("New Tire", response.getBody().get().name());
    }

    @Test
    void testSearchParts() {
        var request = HttpRequest.GET("/api/part/search?q=Test&page=0&size=10");
        var response = client.toBlocking().exchange(request, String.class);
        
        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getBody().orElse(null));
    }

    @Test
    void testDeletePart() {
        var request = HttpRequest.DELETE("/api/part/" + testPart.getId());
        var response = client.toBlocking().exchange(request);
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }
}
