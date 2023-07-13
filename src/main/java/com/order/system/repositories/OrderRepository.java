package com.order.system.repositories;

import com.order.system.repositories.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {
    @Modifying
    @Query(value = "UPDATE `deliver_order` SET order_status = :orderStatus WHERE order_uuid = :orderUuid", nativeQuery = true)
    Integer updateOrderStatusById(@Param("orderUuid") String orderUuid, @Param("orderStatus") String orderStatus);

    @Query(value = "SELECT * FROM `deliver_order` ORDER BY create_date LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<OrderEntity> getOrdersOrderedByCreateDate(@Param("limit") Integer limit, @Param("offset") Integer offset);
}