package com.proovitoo.controller;

import com.proovitoo.DTO.BikePartDTO;
import com.proovitoo.entity.BikePart;
import com.proovitoo.mapper.BikePartMapper;
import com.proovitoo.service.BikePartService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Controller("/api/part")
@RequiredArgsConstructor
public class BikePartController {

    private final BikePartService service;
    private final BikePartMapper mapper;

    @Get("/{id}")
    public BikePartDTO getPartById(@PathVariable UUID id) {
        return mapper.toDto(service.getPartById(id));
    }

    @Get
    public List<BikePartDTO> getAllParts() {
        return mapper.toDtoList(service.getAllParts());
    }

    @Post
    public HttpResponse<BikePartDTO> addPart(@Body @Valid BikePartDTO dto) {
        BikePart saved = service.addPart(dto);
        BikePartDTO body = mapper.toDto(saved);
        return HttpResponse.created(body)
                .headers(h -> h.location(URI.create("/api/parts/" + body.id())));
    }

    @Delete("/{id}")
    public HttpResponse<?> delete(@PathVariable UUID id) {
        service.delete(id);
        return HttpResponse.noContent();
    }

    @Put("/{id}")
    public BikePartDTO update(UUID id, @Body @Valid BikePartDTO dto) {
        return mapper.toDto(service.update(id, dto));
    }
}
