package com.enigmacamp.springbootwmbreview.service;

import com.enigmacamp.springbootwmbreview.dto.request.OrderRequest;
import com.enigmacamp.springbootwmbreview.dto.response.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    OrderResponse createNewTransaction(OrderRequest orderRequest);
    List<OrderResponse> getAll();
    List<OrderResponse> getAllByCustomerId(String is);
    OrderResponse getById(String id);
}
