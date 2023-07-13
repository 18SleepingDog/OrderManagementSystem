package com.order.system.services.impl;

import com.order.system.enums.OrderStatus;
import com.order.system.models.OrderSummary;
import com.order.system.repositories.OrderRepository;
import com.order.system.repositories.entities.OrderEntity;
import com.order.system.services.CustomerService;
import com.order.system.utils.DistanceMatrixAIClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    private OrderRepository orderRepository;

    private  String distanceMatrixAIAccessToken;

    @Autowired
    public CustomerServiceImpl(OrderRepository orderRepository, @Value("${distanceMatrixAI.accessToken}") String distanceMatrixAIAccessToken) {
        this.orderRepository = orderRepository;
        this.distanceMatrixAIAccessToken = distanceMatrixAIAccessToken;
    }

    public OrderSummary createOrder(String originLatitude, String originLongtitude, String destinationLatitude, String destinationLongtitude) throws Exception {
        Integer totalDistance = DistanceMatrixAIClient.getDistanceBetweenLocation(distanceMatrixAIAccessToken, originLatitude, originLongtitude, destinationLatitude, destinationLongtitude);
        OrderEntity saveOrderEntity = OrderEntity.builder()
                .originLatitude(originLatitude)
                .originLongitude(originLongtitude)
                .destinationLatitude(destinationLatitude)
                .destinationLongitude(destinationLongtitude)
                .totalDistance(totalDistance)
                .orderStatus(OrderStatus.UNASSIGNED.name())
                .build();
        OrderEntity orderEntity = orderRepository.save(saveOrderEntity);
        OrderSummary orderSummary = OrderSummary.builder().orderUuid(orderEntity.getOrderUuid()).orderStatus(orderEntity.getOrderStatus()).totalDistance(orderEntity.getTotalDistance()).build();
        return orderSummary;
    }
}
