package com.order.system.controller;

import com.order.system.controllers.CustomerController;
import com.order.system.models.OrderSummary;
import com.order.system.services.CustomerService;
import org.json.JSONArray;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    public void createOrder_givenValidCustomerCreateOrderRequest_returnsSuccess() throws Exception {
        JSONArray originArray = new JSONArray();
        originArray.put("51.4822656");
        originArray.put("-0.1933769");

        JSONArray destinationArray = new JSONArray();
        destinationArray.put("51.4994794");
        destinationArray.put("51.4994794");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("origin", originArray);
        jsonObject.put("destination", destinationArray);

        // Set up the mock CustomerService to return an OrderSummary object
        when(customerService.createOrder("51.4822656", "-0.1933769","51.4994794", "51.4994794"))
                .thenReturn(OrderSummary.builder().orderUuid("1").totalDistance(1).orderStatus("TAKEN").build());

        // Send a POST request to the /orders endpoint with a request body containing the input JSON object
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo("1")))
                .andExpect(jsonPath("$.distance", equalTo(1)))
                .andExpect(jsonPath("$.status", equalTo("TAKEN")));
    }

    private static Stream<Arguments> createOrderInValidCustomerCreateOrderRequestProvider() {
        return Stream.of(
                arguments(new JSONArray().put("200").put("-0.1933769"), new JSONArray().put("51.4994794").put("51.4994794")),
                arguments(new JSONArray().put("51.4994794").put("51.4994794"), new JSONArray().put("200").put("-0.1933769")),
                arguments(new JSONArray().put("-0.1933769").put("-0.1933769").put("-0.1933769"), new JSONArray().put("51.4994794").put("51.4994794")),
                arguments(new JSONArray().put("-0.1933769").put("-0.1933769"), new JSONArray().put("51.4994794").put("51.4994794").put("51.4994794"))
        );
    }

    @ParameterizedTest
    @MethodSource("createOrderInValidCustomerCreateOrderRequestProvider")
    public void createOrder_givenInValidCustomerCreateOrderRequest_throwsValidationException(JSONArray originArray,  JSONArray destinationArray) throws Exception {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("origin", originArray);
        jsonObject.put("destination", destinationArray);


        // Send a POST request to the /orders endpoint with a request body containing the input JSON object
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("must contain exactly two coordinates(latitude, longitude)")));
    }

}
