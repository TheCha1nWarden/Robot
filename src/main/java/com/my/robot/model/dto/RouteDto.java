package com.my.robot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RouteDto {

    private List<PositionDto> route;
    private boolean circular;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        RouteDto routeDto = (RouteDto) obj;
        return route.equals(routeDto.getRoute()) && circular == routeDto.isCircular();
    }

}
