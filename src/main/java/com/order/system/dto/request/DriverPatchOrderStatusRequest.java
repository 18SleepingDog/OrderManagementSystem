package com.order.system.dto.request;

import com.order.system.annotation.DriverPatchOrderStatusPattern;
import com.order.system.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverPatchOrderStatusRequest {
    @JsonProperty("status")
    @NotNull(message = "Status must be provided.")
    @DriverPatchOrderStatusPattern(regexp = "TAKEN|IN_PROGRESS|DELIVERED|RETURNED", message = "Request Order Status is not allowed.")
    private OrderStatus orderStatus;
}
