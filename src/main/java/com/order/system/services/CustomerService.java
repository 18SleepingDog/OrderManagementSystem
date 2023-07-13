package com.order.system.services;

import com.order.system.models.OrderSummary;

public interface CustomerService {
    public OrderSummary createOrder(String originLatitude, String originLongtitude, String destinationLatitude, String destinationLongtitude) throws Exception;
}
