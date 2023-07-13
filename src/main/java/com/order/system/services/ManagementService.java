package com.order.system.services;

import com.order.system.models.OrderSummary;

import java.util.List;

public interface ManagementService {
    public List<OrderSummary> getOrderSummaryList(Integer limit, Integer offset);
}
