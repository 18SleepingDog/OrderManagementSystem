package com.order.system.controllers;

import com.order.system.dto.request.CustomerCreateOrderRequest;
import com.order.system.exceptions.ValidationException;
import com.order.system.models.OrderSummary;
import com.order.system.services.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Processes a customer request to create a new order and returns a response entity with the order summary.
     *
     * @param customerCreateOrderRequest the request object that contains the details of the order to be created
     * @return a ResponseEntity with the order summary
     * @throws ValidationException if the request validation fails due to invalid input data
     * @throws Exception if an error occurs while processing the order request
     */
    @PostMapping("/orders")
    public ResponseEntity<OrderSummary> createOrder(@RequestBody @Valid CustomerCreateOrderRequest customerCreateOrderRequest, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            List<ObjectError> errors = bindingResult.getAllErrors();
            String errorMessage = errors.stream().map(objectError -> objectError.getDefaultMessage() + ";").reduce("", String::concat);
            log.error("[createOrder] - Received Unexpected POST Request to \"orders\"");
            throw new ValidationException(errorMessage);
        }
        log.info("[createOrder] - Received POST Request to \"orders\" with customer create order request: {}", customerCreateOrderRequest);
        String originLatitude = customerCreateOrderRequest.getOriginLatitudeLongitudeListPattern().get(0);
        String originLongtitude = customerCreateOrderRequest.getOriginLatitudeLongitudeListPattern().get(1);
        String destinationLatitude = customerCreateOrderRequest.getDestinationLatitudeLongitudeListPattern().get(0);
        String destinationLongtitude = customerCreateOrderRequest.getDestinationLatitudeLongitudeListPattern().get(1);
        OrderSummary orderSummary = customerService.createOrder(originLatitude, originLongtitude, destinationLatitude, destinationLongtitude);
        return ResponseEntity.ok(orderSummary);
    }

}
