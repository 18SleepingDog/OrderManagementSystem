package com.order.system.repositories;

import com.order.system.repositories.entities.OrderStatusOperationLockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface OrderStatusOperationLockRepository extends JpaRepository<OrderStatusOperationLockEntity, String> {
    @Modifying
    @Query(value = "INSERT INTO `deliver_order_status_operation_lock` (order_uuid) VALUES (:orderUuid)", nativeQuery = true)
    Integer insertOrderUuid(@Param("orderUuid") String orderUuid);

    @Modifying
    @Query(value = "DELETE FROM `deliver_order_status_operation_lock` WHERE create_date < DATE_SUB(NOW(), INTERVAL 5 MINUTE)", nativeQuery = true)
    Integer deleteOrphanedKeyLocks();
}