package com.order.system.models;

import com.order.system.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    private String orderUuid;

    private OrderStatus status;

    private String originLatitude;

    private String originLongitude;

    private String destinationLatitude;

    private String destinationLongitude;

    private Integer totalDistance;

    private Timestamp createDate;

    private Instant lastModifiedDate;
}
