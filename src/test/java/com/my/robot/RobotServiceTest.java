package com.my.robot;

import com.my.robot.model.Command;
import com.my.robot.model.Position;
import com.my.robot.model.Robot;
import com.my.robot.model.Vector;
import com.my.robot.model.dto.PositionDto;
import com.my.robot.model.dto.RouteDto;
import com.my.robot.model.mapper.PositionDtoMapper;
import com.my.robot.repository.PositionRepository;
import com.my.robot.repository.RobotRepository;
import com.my.robot.service.RobotService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class RobotServiceTest {

    private RobotService robotService;

    @Mock
    private RobotRepository robotRepository;
    @Mock
    private PositionRepository positionRepository;
    @Mock
    private PositionDtoMapper positionDtoMapper;

    @BeforeEach
    public void init() {
        robotService = new RobotService(robotRepository, positionRepository, positionDtoMapper);
    }


    @ParameterizedTest
    @MethodSource("dataForTestGetPositionSuccess")
    public void getPositionSuccess(Robot robot, PositionDto expectedPosition) {
        Mockito.when(robotRepository.getCurrent()).thenReturn(robot);
        Mockito.when(positionDtoMapper.map(robot.getPosition())).thenReturn(expectedPosition);
        Assertions.assertEquals(expectedPosition, robotService.getPosition());
    }

    @Test
    public void getRouteSuccess() {
        List<Robot> robots = List.of(new Robot(new Position(0, 0),
                Vector.NORTH, null), new Robot(new Position(0, 1),
                Vector.NORTH, Command.G), new Robot(new Position(0, 1),
                Vector.WEST, Command.L));
        List<Position> positions = List.of(new Position(0, 0), new Position(0, 1));
        Mockito.when(robotRepository.findAll()).thenReturn(robots);
        Mockito.when(positionRepository.findAll()).thenReturn(positions);
        Mockito.when(positionDtoMapper.map(positions.get(0))).thenReturn(new PositionDto(0, 0));
        Mockito.when(positionDtoMapper.map(positions.get(1))).thenReturn(new PositionDto(0, 1));
        Assertions.assertEquals(robotService.getRoute(),
                new RouteDto(List.of(new PositionDto(0, 0), new PositionDto(0, 1)),
                        true));
    }

    private static Stream<Arguments> dataForTestGetPositionSuccess() {
        return Stream.of(Arguments.arguments(new Robot(new Position(0, 0),
                                Vector.NORTH, null),
                        new PositionDto(0, 0)),
                Arguments.arguments(new Robot(new Position(3, 5),
                                Vector.NORTH, Command.G),
                        new PositionDto(3, 5)),
                Arguments.arguments(new Robot(new Position(-2, -8),
                                Vector.NORTH, Command.R),
                        new PositionDto(-2, -8))
        );
    }

}
