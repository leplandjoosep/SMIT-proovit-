package com.proovitoo.repository;

import com.proovitoo.entity.VinylRecord;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.H2)
public interface VinylRecordRepo extends PageableRepository<VinylRecord, UUID> {

    Optional<VinylRecord> findByFingerprint(String fingerprint);

    @Query(
            value = """
            SELECT vinyl_record_.* FROM vinyl_record vinyl_record_
            WHERE
              (:q IS NULL OR
                 LOWER(vinyl_record_.title)  LIKE LOWER(CONCAT('%', :q, '%')) OR
                 LOWER(vinyl_record_.artist) LIKE LOWER(CONCAT('%', :q, '%')))
              AND (:artist IS NULL OR LOWER(vinyl_record_.artist) = LOWER(:artist))
            """,
            countQuery = """
            SELECT COUNT(*) FROM vinyl_record vinyl_record_
            WHERE
              (:q IS NULL OR
                 LOWER(vinyl_record_.title)  LIKE LOWER(CONCAT('%', :q, '%')) OR
                 LOWER(vinyl_record_.artist) LIKE LOWER(CONCAT('%', :q, '%')))
              AND (:artist IS NULL OR LOWER(vinyl_record_.artist) = LOWER(:artist))
            """
    )
    Page<VinylRecord> search(@Nullable String q,
                             @Nullable String artist,
                             Pageable pageable);

    @Query("SELECT DISTINCT artist FROM vinyl_record WHERE artist IS NOT NULL ORDER BY artist")
    List<String> distinctArtists();
}
