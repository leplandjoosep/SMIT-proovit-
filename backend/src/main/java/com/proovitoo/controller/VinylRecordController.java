package com.proovitoo.controller;

import com.proovitoo.DTO.VinylRecordDTO;
import com.proovitoo.entity.VinylRecord;
import com.proovitoo.mapper.VinylRecordMapper;
import com.proovitoo.service.VinylRecordService;
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

@Controller("/api/vinyl")
@RequiredArgsConstructor
public class VinylRecordController {

    private final VinylRecordService service;
    private final VinylRecordMapper mapper;
    @Get("/{id}")
    public VinylRecordDTO getVinylById(@PathVariable UUID id) {
        return mapper.toDto(service.getById(id));
    }

    @Get
    public List<VinylRecordDTO> getAllVinyls() {
        return mapper.toDtoList(service.getAllVinyls());
    }

    @Get("/search")
    public Page<VinylRecordDTO> search(@QueryValue @Nullable String q,
                                       @QueryValue @Nullable String artist,
                                       Pageable pageable) {
        return service.search(q, artist, pageable).map(mapper::toDto);
    }

    @Get("/artists")
    public List<String> artists() {
        return service.listArtists();
    }

    @Post
    public HttpResponse<VinylRecordDTO> addVinyl(@Body @Valid VinylRecordDTO vinylRecordDTO) {
        VinylRecord saved = service.addVinyl(vinylRecordDTO);
        VinylRecordDTO body  = mapper.toDto(saved);
        return HttpResponse.created(body)
                .headers(h -> h.location(URI.create("/api/vinyls/" + body.id())));
    }

    @Delete("/{id}")
    public HttpResponse<?> delete(@PathVariable UUID id) {
        service.delete(id);
        return HttpResponse.noContent();
    }

    @Put("/{id}")
    public VinylRecordDTO update(UUID id, @Body @Valid VinylRecordDTO dto) {
        return mapper.toDto(service.update(id, dto));
    }
}
