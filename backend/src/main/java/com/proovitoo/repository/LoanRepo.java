package com.proovitoo.repository;

import com.proovitoo.entity.Loan;
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
public interface LoanRepo extends PageableRepository<Loan, UUID> {

    long countByBikePartIdAndReturnedAtIsNull(UUID partId);
}
