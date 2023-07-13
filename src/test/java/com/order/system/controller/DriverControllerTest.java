package com.order.system.controller;

import com.order.system.controllers.DriverController;
import com.order.system.enums.OrderStatus;
import com.order.system.exceptions.DriverPatchOrderStatusException;
import com.order.system.services.DriverService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DriverController.class)
public class DriverControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DriverService driverService;

    @Test
    public void driverPatchOrderStatus_givenValidOrderStatus_returnsSuccess() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "TAKEN");

        doNothing().when(driverService).driverPatchOrderStatus("orderUuid", OrderStatus.TAKEN);

        mockMvc.perform(patch("/orders/{id}", "orderUuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo("SUCCESS")));
    }

    @Test
    public void driverPatchOrderStatus_givenInvalidOrderStatus_throwsValidationException() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "UNASSIGNED");

        // Set up the mock DriverService to return a success response
        doNothing().when(driverService).driverPatchOrderStatus("orderUuid", OrderStatus.TAKEN);

        mockMvc.perform(patch("/orders/{id}", "orderUuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Request Order Status is not allowed.")));
    }

    private static Stream<Arguments> driverPatchOrderStatusUnexpectedResultInUpdateStatusProvider() {
        return Stream.of(
                Arguments.arguments(new DriverPatchOrderStatusException("Request Order Status does not exist.")),
                arguments(new DriverPatchOrderStatusException("Request Order is already taken.")),
                arguments(new DriverPatchOrderStatusException("Fail to update request order. Please try again."))
        );
    }

    @ParameterizedTest
    @MethodSource("driverPatchOrderStatusUnexpectedResultInUpdateStatusProvider")
    public void driverPatchOrderStatus_givenUnexpectedResultInUpdateStatus_throwsDriverPatchOrderStatusException(DriverPatchOrderStatusException driverPatchOrderStatusException) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "TAKEN");

        doThrow(driverPatchOrderStatusException).when(driverService).driverPatchOrderStatus("orderUuid", OrderStatus.TAKEN);

        mockMvc.perform(patch("/orders/{id}", "orderUuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString(driverPatchOrderStatusException.getMessage())));
    }
}
