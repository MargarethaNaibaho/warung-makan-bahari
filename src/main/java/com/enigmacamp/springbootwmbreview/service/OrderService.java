package com.enigmacamp.springbootwmbreview.service;

import com.enigmacamp.springbootwmbreview.dto.request.OrderRequest;
import com.enigmacamp.springbootwmbreview.dto.response.OrderResponse;
import com.enigmacamp.springbootwmbreview.entity.Order;

import java.util.List;

public interface OrderService {
    OrderResponse createNewTransaction(OrderRequest orderRequest);
    List<OrderResponse> getAll();
    OrderResponse getById(String id);
}
