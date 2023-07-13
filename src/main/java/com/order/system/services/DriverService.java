package com.order.system.services;

import com.order.system.enums.OrderStatus;

public interface DriverService {
    public void driverPatchOrderStatus(String orderUuid, OrderStatus orderStatus) throws Exception;
}
