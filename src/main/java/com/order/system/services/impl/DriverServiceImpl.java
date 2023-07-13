package com.order.system.services.impl;

import com.order.system.enums.OrderStatus;
import com.order.system.exceptions.DriverPatchOrderStatusException;
import com.order.system.repositories.OrderRepository;
import com.order.system.repositories.OrderStatusOperationLockRepository;
import com.order.system.repositories.entities.OrderEntity;
import com.order.system.services.DriverService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class DriverServiceImpl implements DriverService {

    private OrderRepository orderRepository;

    private OrderStatusOperationLockRepository orderStatusOperationLockRepository;

    @Autowired
    public DriverServiceImpl(OrderRepository orderRepository, OrderStatusOperationLockRepository orderStatusOperationLockRepository) {
        this.orderRepository = orderRepository;
        this.orderStatusOperationLockRepository = orderStatusOperationLockRepository;
    }

    @Transactional
    public void driverPatchOrderStatus(String orderUuid, OrderStatus orderStatus) throws Exception {
        // create lock
        orderStatusOperationLockRepository.insertOrderUuid(orderUuid);
        // check the order already taken or not
        Optional<OrderEntity> targetOrder = orderRepository.findById(orderUuid);
        if(!targetOrder.isPresent()){
            orderStatusOperationLockRepository.deleteById(orderUuid);
            log.error("[driverPatchOrderStatus] - Request orderUuid does not exist. Fail to update orderUuid: {} order status to TAKEN", orderUuid);
            throw new DriverPatchOrderStatusException("Request Order does not exist.");
        } else if (targetOrder.get().getOrderStatus().equals(OrderStatus.TAKEN.name())) {
            orderStatusOperationLockRepository.deleteById(orderUuid);
            log.error("[driverPatchOrderStatus] - Request orderUuid already taken. Fail to update orderUuid: {} order status to TAKEN", orderUuid);
            throw new DriverPatchOrderStatusException("Request Order is already taken.");
        }else{
            if(orderRepository.updateOrderStatusById(orderUuid, orderStatus.name()) == 1){
                // release lock
                orderStatusOperationLockRepository.deleteById(orderUuid);
                log.info("[driverPatchOrderStatus] - Request orderUuid:{} updates to taken.", orderUuid);
                return;
            }
            orderStatusOperationLockRepository.deleteById(orderUuid);
            log.error("[driverPatchOrderStatus] - Fail to update orderUuid: {} order status to TAKEN", orderUuid);
            throw new DriverPatchOrderStatusException("Fail to update request order. Please try again.");
        }
    }
}
