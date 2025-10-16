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

import java.util.UUID;

@JdbcRepository(dialect = Dialect.H2)
public interface BikePartRepo extends PageableRepository<BikePart, UUID> {

}
