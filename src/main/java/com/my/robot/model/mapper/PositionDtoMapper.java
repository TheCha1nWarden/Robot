package com.my.robot.model.mapper;

import com.my.robot.model.Position;
import com.my.robot.model.dto.PositionDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PositionDtoMapper {

    PositionDto map(Position position);

}
