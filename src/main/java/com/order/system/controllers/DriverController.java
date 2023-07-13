package com.order.system.controllers;

import com.order.system.dto.request.DriverPatchOrderStatusRequest;
import com.order.system.dto.response.DriverPatchOrderStatusResponse;
import com.order.system.exceptions.ValidationException;
import com.order.system.services.DriverService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
public class DriverController {

    private DriverService driverService;

    @Autowired
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PatchMapping("/orders/{id}")
    public ResponseEntity<DriverPatchOrderStatusResponse> driverPatchOrderStatus(@PathVariable("id") String orderUuid, @Valid @RequestBody DriverPatchOrderStatusRequest driverPatchOrderStatusRequest, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            List<ObjectError> errors = bindingResult.getAllErrors();
            String errorMessage = errors.stream().map(objectError -> objectError.getDefaultMessage() + ";").reduce("", String::concat);
            log.error("[driverPatchOrderStatus] - Received PATCH Request  to \"orders\"/{id} with orderUuid: {}", orderUuid);
            throw new ValidationException(errorMessage);
        }
        log.info("[driverPatchOrderStatus] - Received PATCH Request to \"orders\"/{id} with driver patch order status request: {} with orderUuid: {}", driverPatchOrderStatusRequest, orderUuid);
        driverService.driverPatchOrderStatus(orderUuid, driverPatchOrderStatusRequest.getOrderStatus());
        DriverPatchOrderStatusResponse driverPatchOrderStatusResponse = DriverPatchOrderStatusResponse.builder().status("SUCCESS").build();
        return ResponseEntity.ok(driverPatchOrderStatusResponse);

    }
}
