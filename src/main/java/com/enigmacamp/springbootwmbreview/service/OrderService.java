package com.enigmacamp.springbootwmbreview.service;

import com.enigmacamp.springbootwmbreview.dto.request.OrderRequest;
import com.enigmacamp.springbootwmbreview.entity.Order;

public interface OrderService {
    Order createNewTransaction(OrderRequest orderRequest);
}
