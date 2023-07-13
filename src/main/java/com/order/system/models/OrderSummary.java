package com.order.system.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderSummary {
    @JsonProperty("id")
    private String orderUuid;

    @JsonProperty("distance")
    private Integer totalDistance;

    @JsonProperty("status")
    private String orderStatus;
}
