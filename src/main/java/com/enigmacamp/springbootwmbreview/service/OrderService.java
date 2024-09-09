package com.enigmacamp.springbootwmbreview.service;

import com.enigmacamp.springbootwmbreview.entity.Order;

public interface OrderService {
    Order createNewTransaction(Order order);
}
