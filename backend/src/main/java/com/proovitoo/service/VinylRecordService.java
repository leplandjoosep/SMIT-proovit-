package com.proovitoo.service;


import com.proovitoo.DTO.VinylRecordDTO;
import com.proovitoo.entity.VinylRecord;
import com.proovitoo.mapper.VinylRecordMapper;
import com.proovitoo.repository.VinylRecordRepo;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class VinylRecordService {

    private final VinylRecordRepo repo;
    private final VinylRecordMapper mapper;

    public VinylRecord getById(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, "Vinyl not found"));
    }

    public List<VinylRecord> getAllVinyls() {
        return repo.findAll();
    }

    public VinylRecord addVinyl(VinylRecordDTO vinylRecordDTO) {
        VinylRecord vinyl = mapper.toEntity(vinylRecordDTO);

        String fp = computeFingerprint(vinyl);
        vinyl.setFingerprint(fp);

        repo.findByFingerprint(fp).ifPresent(v -> {
            throw new HttpStatusException(HttpStatus.CONFLICT, "Duplicate vinyl (artist|title|year)");
        });

        return repo.save(vinyl);
    }

    private static String computeFingerprint(VinylRecord vinyl) {
        return String.join("|",
                norm(vinyl.getArtist()),
                norm(vinyl.getTitle()),
                vinyl.getReleaseYear() == null ? "" : vinyl.getReleaseYear().toString()
        );
    }

    private static String norm(String s) {
        if (s == null) return "";
        String normalized = java.text.Normalizer.normalize(s.trim().toLowerCase(), java.text.Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}+", "");
    }

    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Vinyl not found");
        }
        repo.deleteById(id);
    }

    public VinylRecord update(UUID id, VinylRecordDTO dto) {
        VinylRecord v = getById(id);

        v.setTitle(dto.title());
        v.setArtist(dto.artist());
        v.setReleaseYear(dto.releaseYear());
        v.setAcquiredFrom(dto.acquiredFrom());
        v.setAcquiredDate(dto.acquiredDate());
        v.setLocation(dto.location());
        v.setNotes(dto.notes());

        String fp = computeFingerprint(v);
        if (!fp.equals(v.getFingerprint())) {
            repo.findByFingerprint(fp).ifPresent(other -> {
                if (!other.getId().equals(id)) {
                    throw new HttpStatusException(HttpStatus.CONFLICT, "Duplicate vinyl (artist+title+year)");
                }
            });
            v.setFingerprint(fp);
        }

        return repo.update(v);
    }
}
