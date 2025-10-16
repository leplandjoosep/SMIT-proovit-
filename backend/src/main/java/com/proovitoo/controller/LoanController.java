package com.proovitoo.controller;

import com.proovitoo.DTO.LoanDTO;
import com.proovitoo.entity.Loan;
import com.proovitoo.mapper.LoanMapper;
import com.proovitoo.service.LoanService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Controller("/api/loan")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService service;
    private final LoanMapper mapper;

    @Get("/{id}")
    public LoanDTO getLoanById(UUID id) {
        return mapper.toDto(service.getLoanById(id));
    }

    @Get
    public List<LoanDTO> getAllLoans() {
        return mapper.toDtoList(service.getAllLoans());
    }

    @Post
    public HttpResponse<LoanDTO> addLoan(@Body @Valid LoanDTO dto) {
        Loan saved = service.addLoan(dto);
        LoanDTO body = mapper.toDto(saved);
        return HttpResponse.created(body)
                .headers(h -> h.location(URI.create("/api/loan/" + body.id())));
    }
}
