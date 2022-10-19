package com.my.robot.controller;

import com.my.robot.model.RequestCommand;
import com.my.robot.model.dto.PositionDto;
import com.my.robot.model.dto.RouteDto;
import com.my.robot.service.RobotService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/robot")
@AllArgsConstructor
public class RobotController {

    private static final Random random = new Random();
    private static final int MILLISECONDS_IN_A_SECOND = 1000;
    private static final int MAX_SECOND_DELAY = 10;
    private final RobotService robotService;

    @GetMapping("/position")
    public PositionDto getPosition() {
        try {
            Thread.sleep((random.nextInt(MAX_SECOND_DELAY) + 1) * MILLISECONDS_IN_A_SECOND);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return robotService.getPosition();
    }

    @GetMapping("/route")
    public RouteDto getRoute() {
        return robotService.getRoute();
    }

    @PostMapping("/reset")
    public void reset() {
        new Thread(() -> {
            try {
                Thread.sleep((random.nextInt(MAX_SECOND_DELAY) + 1) * MILLISECONDS_IN_A_SECOND);
                robotService.reset();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @PostMapping("/command")
    public void doCommand(@RequestBody RequestCommand requestCommand) {
        new Thread(() -> {
            try {
                Thread.sleep((random.nextInt(MAX_SECOND_DELAY) + 1) * MILLISECONDS_IN_A_SECOND);
                robotService.doCommand(requestCommand);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
