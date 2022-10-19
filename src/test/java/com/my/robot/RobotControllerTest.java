package com.my.robot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.robot.controller.RobotController;
import com.my.robot.model.Command;
import com.my.robot.model.RequestCommand;
import com.my.robot.model.dto.PositionDto;
import com.my.robot.model.dto.RouteDto;
import com.my.robot.service.RobotService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RobotController.class)
public class RobotControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    RobotService robotService;

    @Test
    public void getPositionSuccess() throws Exception {
        Mockito.when(robotService.getPosition()).thenReturn(new PositionDto(0, 1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/robot/position")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.x", Matchers.is(0)))
                .andExpect(jsonPath("$.y", Matchers.is(1)));
    }

    @Test
    public void getRouteSuccess() throws Exception {
        Mockito.when(robotService.getRoute())
                .thenReturn(new RouteDto(Arrays.asList(new PositionDto(0, 0),
                        new PositionDto(0, 1)), true));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/robot/route")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.route", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.route[0].x", Matchers.is(0)))
                .andExpect(jsonPath("$.route[0].y", Matchers.is(0)))
                .andExpect(jsonPath("$.route[1].x", Matchers.is(0)))
                .andExpect(jsonPath("$.route[1].y", Matchers.is(1)))
                .andExpect(jsonPath("$.circular", Matchers.is(true)));
    }

    @Test
    public void doCommandSuccess() throws Exception {
        RequestCommand requestCommand = new RequestCommand(Command.G);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/robot/command")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestCommand));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void doResetSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/robot/reset"))
                .andExpect(status().isOk());
    }

}
