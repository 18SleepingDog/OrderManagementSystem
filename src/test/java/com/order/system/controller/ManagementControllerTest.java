package com.order.system.controller;

import com.order.system.controllers.ManagementController;
import com.order.system.models.OrderSummary;
import com.order.system.services.ManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManagementController.class)
public class ManagementControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManagementService managementService;

    @Test
    public void managementGetOrderList_givenValidParams_returnsSuccess() throws Exception {

        List<OrderSummary> expectedOrderSummaryList = List.of(
                new OrderSummary("1",1, "TAKEN"),
                new OrderSummary("2",2, "TAKEN"),
                new OrderSummary("3",3, "TAKEN"),
                new OrderSummary("4",4, "TAKEN"),
                new OrderSummary("5",5, "TAKEN")
        );

        when(managementService.getOrderSummaryList(5, 0)).thenReturn(expectedOrderSummaryList);

        mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "1")
                        .param("limit", "5")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(expectedOrderSummaryList.size())))
                .andExpect(jsonPath("$[0].id").value(expectedOrderSummaryList.get(0).getOrderUuid()))
                .andExpect(jsonPath("$[0].distance").value(expectedOrderSummaryList.get(0).getTotalDistance()))
                .andExpect(jsonPath("$[0].status").value(expectedOrderSummaryList.get(0).getOrderStatus()));
    }

    @Test
    public void managementGetOrderList_givenInvalidPageParam_throwsValidationException() throws Exception {

        mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("limit", "5")
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Page cannot smaller than 1.")));
    }

}
