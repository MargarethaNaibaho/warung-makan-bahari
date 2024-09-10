package com.enigmacamp.springbootwmbreview.controller;

import com.enigmacamp.springbootwmbreview.dto.request.OrderRequest;
import com.enigmacamp.springbootwmbreview.entity.Order;
import com.enigmacamp.springbootwmbreview.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/api/v1/transaction")
    public Order createNewTransaction(@RequestBody OrderRequest orderRequest){
        return orderService.createNewTransaction(orderRequest);
    }
}
