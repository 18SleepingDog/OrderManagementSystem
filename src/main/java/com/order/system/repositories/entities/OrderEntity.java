package com.order.system.repositories.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "deliver_order")
public class OrderEntity {

    @Id
    @UuidGenerator
    @Column(name = "order_uuid", unique = true, nullable = false, updatable = false)
    private String orderUuid;

    @Column(name = "order_status")
    private String orderStatus;

    @Column(name = "origin_latitude", nullable = false)
    private String originLatitude;

    @Column(name = "origin_longitude", nullable = false)
    private String originLongitude;

    @Column(name = "destination_latitude", nullable = false)
    private String destinationLatitude;

    @Column(name = "destination_longitude", nullable = false)
    private String destinationLongitude;

    @Column(name = "total_distance", nullable = false)
    private Integer totalDistance;

    @Column(name = "create_date")
    private Timestamp createDate;

    @Column(name = "last_modified_date")
    @UpdateTimestamp
    private Timestamp  lastModifiedDate;

}