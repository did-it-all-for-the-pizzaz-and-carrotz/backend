package tech.carrotly.restapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SampleMapper {
    SampleMapper INSTANCE = Mappers.getMapper(SampleMapper.class);
}
