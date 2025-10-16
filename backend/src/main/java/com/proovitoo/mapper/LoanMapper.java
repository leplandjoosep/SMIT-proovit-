package com.proovitoo.mapper;

import com.proovitoo.DTO.LoanDTO;
import com.proovitoo.entity.Loan;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "jsr330", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LoanMapper {

    @Mapping(source = "bikePart.id", target = "bikePartId")
    LoanDTO toDto(Loan entity);

    List<LoanDTO> toDtoList(List<Loan> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bikePart", ignore = true)
    @Mapping(target = "borrowedAt", ignore = true)
    Loan toEntity(LoanDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bikePart", ignore = true)
    @Mapping(target = "borrowedAt", ignore = true)
    void updateEntityFromDto(LoanDTO dto, @MappingTarget Loan entity);
}
