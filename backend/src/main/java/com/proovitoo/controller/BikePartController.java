package com.proovitoo.controller;

import com.proovitoo.DTO.BikePartDTO;
import com.proovitoo.entity.BikePart;
import com.proovitoo.mapper.BikePartMapper;
import com.proovitoo.service.BikePartService;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

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

    @Get("/search")
    public Page<BikePartDTO> search(@QueryValue @Nullable String q,
                                    @QueryValue @Nullable String category,
                                    Pageable pageable) {
        return service.search(q, category, pageable).map(mapper::toDto);
    }

    @Get("/categories")
    public List<String> categories() {
        return service.listCategories();
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
