package com.proovitoo.repository;

import com.proovitoo.entity.BikePart;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;

import java.util.List;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.H2)
public interface BikePartRepo extends PageableRepository<BikePart, UUID> {

    @Query(
            value = """
            SELECT bike_part_.* FROM bike_part bike_part_
            WHERE
              (:q IS NULL OR
                 LOWER(bike_part_.name)  LIKE LOWER(CONCAT('%', :q, '%')) OR
                 LOWER(bike_part_.brand) LIKE LOWER(CONCAT('%', :q, '%')))
              AND (:category IS NULL OR LOWER(bike_part_.category) = LOWER(:category))
            """,
            countQuery = """
            SELECT COUNT(*) FROM bike_part bike_part_
            WHERE
              (:q IS NULL OR
                 LOWER(bike_part_.name)  LIKE LOWER(CONCAT('%', :q, '%')) OR
                 LOWER(bike_part_.brand) LIKE LOWER(CONCAT('%', :q, '%')))
              AND (:category IS NULL OR LOWER(bike_part_.category) = LOWER(:category))
            """
    )
    Page<BikePart> search(@Nullable String q,
                          @Nullable String category,
                          Pageable pageable);

    @Query("SELECT DISTINCT category FROM bike_part WHERE category IS NOT NULL ORDER BY category")
    List<String> distinctCategories();
}
