package com.order.system.controllers;

import com.order.system.exceptions.ValidationException;
import com.order.system.models.OrderSummary;
import com.order.system.services.ManagementService;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Validated
@RestController
public class ManagementController {

    private ManagementService managementService;

    @Autowired
    public ManagementController(ManagementService managementService) {
        this.managementService = managementService;
    }

    /**
     * Retrieves a list of order summaries based on the specified pagination parameters.
     *
     * @param page The page number of the results to retrieve. Must be greater than or equal to 1.
     * @param limit The maximum number of results to retrieve.
     * @return A ResponseEntity containing a list of OrderSummary objects.
     * @throws ValidationException If the specified page parameter is less than 1.
     */
    @GetMapping("/orders")
    public ResponseEntity<List<OrderSummary>> managementGetOrderList(@RequestParam(value = "page")  @Min(value = 1, message = "Page cannot smaller than 1.") int page, @RequestParam(value = "limit") int limit) {
        log.info("[managementGetOrderList] - Received GET Request to \"orders\" with page: {} and limit: {}.", page, limit);
        Integer offset = (page - 1) * limit;
        return ResponseEntity.ok(managementService.getOrderSummaryList(limit, offset));
    }
}
