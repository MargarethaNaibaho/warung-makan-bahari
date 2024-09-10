package com.enigmacamp.springbootwmbreview.controller;

import com.enigmacamp.springbootwmbreview.dto.request.OrderRequest;
import com.enigmacamp.springbootwmbreview.dto.response.OrderDetailResponse;
import com.enigmacamp.springbootwmbreview.dto.response.OrderResponse;
import com.enigmacamp.springbootwmbreview.entity.Order;
import com.enigmacamp.springbootwmbreview.service.OrderDetailService;
import com.enigmacamp.springbootwmbreview.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    @PostMapping("/api/v1/transaction")
    public OrderResponse createNewTransaction(@RequestBody OrderRequest orderRequest){
        return orderService.createNewTransaction(orderRequest);
    }

    @GetMapping("/api/v1/transaction")
    public List<OrderResponse> getAllTransactions(){
        return orderService.getAll();
    }

    @GetMapping("/api/v1/transaction/{id}")
    public OrderResponse getTransactionById(@PathVariable String id){
        return orderService.getById(id);
    }

    @GetMapping("/api/v1/transaction/detail/{id}")
    public OrderDetailResponse getOrderDetailById(@PathVariable String id){
        return  orderDetailService.getById(id);
    }
}
