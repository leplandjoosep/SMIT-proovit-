package com.proovitoo.mapper;

import com.proovitoo.DTO.BikePartDTO;
import com.proovitoo.entity.BikePart;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "jsr330", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BikePartMapper {

    BikePartDTO toDto(BikePart entity);

    List<BikePartDTO> toDtoList(List<BikePart> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    BikePart toEntity(BikePartDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(BikePartDTO dto, @MappingTarget BikePart entity);
}
