package com.order.system.services.impl;

import com.order.system.models.OrderSummary;
import com.order.system.repositories.OrderRepository;
import com.order.system.repositories.entities.OrderEntity;
import com.order.system.services.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagementServiceImpl implements ManagementService {
    private OrderRepository orderRepository;

    @Autowired
    public ManagementServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderSummary> getOrderSummaryList(Integer limit, Integer offset) {
        List<OrderEntity> orderEntityList = orderRepository.getOrdersOrderedByCreateDate(limit, offset);
        List<OrderSummary> orderSummaryList = orderEntityList
                .stream()
                .map(orderEntity -> new OrderSummary(orderEntity.getOrderUuid(), orderEntity.getTotalDistance(), orderEntity.getOrderStatus()))
                .collect(Collectors.toList());
        return orderSummaryList;
    }
}
