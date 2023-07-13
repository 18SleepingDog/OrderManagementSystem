package com.order.system.repositories.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "deliver_order_status_operation_lock")
public class OrderStatusOperationLockEntity {
    @Id
    @Column(name = "order_uuid", unique = true, nullable = false, updatable = false)
    private String orderUuid;

    @Column(name = "create_date")
    private Timestamp createDate;
}
