package com.my.robot.service;

import com.my.robot.model.*;
import com.my.robot.model.dto.PositionDto;
import com.my.robot.model.dto.RouteDto;
import com.my.robot.model.mapper.PositionDtoMapper;
import com.my.robot.repository.PositionRepository;
import com.my.robot.repository.RobotRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RobotService {

    private static final String ILLEGAL_ARGUMENT_EXCEPTION_MSG = "Incorrect command";
    private static final int MAX_NUMBER_OF_REPETITIONS_TO_GET_CIRCULAR = 4;
    private final RobotRepository robotRepository;
    private final PositionRepository positionRepository;
    private final PositionDtoMapper mapper;

    public synchronized void doCommand(RequestCommand requestCommand) {
        Command command = requestCommand.getCommand();
        Robot currentRobot = robotRepository.getCurrent();
        Position currentPosition = currentRobot.getPosition();
        Vector currentVector = currentRobot.getVector();
        switch (command) {
            case L, R -> {
                Vector newVector = Vector.doTurn(command, currentVector);
                robotRepository.save(new Robot(currentPosition, newVector, command));
            }
            case G -> {
                Position newPosition = new Position(
                        currentPosition.getX() + currentVector.getPosition().getX(),
                        currentPosition.getY() + currentVector.getPosition().getY());
                positionRepository.save(newPosition);
                robotRepository.save(new Robot(newPosition, currentVector, command));
            }
            default -> throw new IllegalArgumentException(ILLEGAL_ARGUMENT_EXCEPTION_MSG);
        }
    }

    public synchronized PositionDto getPosition() {
        return mapper.map(robotRepository.getCurrent().getPosition());
    }

    @Transactional
    public synchronized void reset() {
        robotRepository.reset();
        positionRepository.reset();
    }

    public RouteDto getRoute() {
        List<PositionDto> route = positionRepository.findAll().stream()
                .map(mapper::map).collect(Collectors.toList());
        List<Command> commands = robotRepository.findAll().stream()
                .map(Robot::getLastCommand).filter(Objects::nonNull).collect(Collectors.toList());
        return new RouteDto(route, checkCircular(commands));
    }

    private boolean checkCircular(List<Command> commands) {
        boolean output = false;
        Position tmpPosition = new Position(0, 0);
        Vector tmpVector = Vector.NORTH;
        for (int i = 0; i < MAX_NUMBER_OF_REPETITIONS_TO_GET_CIRCULAR; i++) {
            for (Command command : commands) {
                switch (command) {
                    case L, R -> tmpVector = Vector.doTurn(command, tmpVector);
                    case G -> {
                        tmpPosition.setX(tmpPosition.getX() + tmpVector.getPosition().getX());
                        tmpPosition.setY(tmpPosition.getY() + tmpVector.getPosition().getY());
                    }
                    default -> throw new IllegalArgumentException(ILLEGAL_ARGUMENT_EXCEPTION_MSG);
                }
            }
            if (tmpPosition.equals(new Position(0, 0)) && tmpVector.equals(Vector.NORTH)) {
                output = true;
                break;
            }
        }
        return output;
    }

}
