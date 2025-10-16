package com.proovitoo.mapper;

import com.proovitoo.DTO.VinylRecordDTO;
import com.proovitoo.entity.VinylRecord;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "jsr330", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VinylRecordMapper {

    VinylRecordDTO toDto(VinylRecord record);

    List<VinylRecordDTO> toDtoList(List<VinylRecord> records);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fingerprint", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    VinylRecord toEntity(VinylRecordDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fingerprint", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(VinylRecordDTO dto, @MappingTarget VinylRecord entity);
}
