package com.enigmacamp.springbootwmbreview.service.impl;

import com.enigmacamp.springbootwmbreview.dto.response.OrderDetailResponse;
import com.enigmacamp.springbootwmbreview.entity.OrderDetail;
import com.enigmacamp.springbootwmbreview.repository.OrderDetailRepository;
import com.enigmacamp.springbootwmbreview.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderDetail> createBulk(List<OrderDetail> orderDetails) {
        return orderDetailRepository.saveAllAndFlush(orderDetails);
    }

    @Override
    public OrderDetailResponse getById(String id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElseThrow(() -> new RuntimeException("Order detail isn't found!"));
        OrderDetailResponse orderDetailResponse = OrderDetailResponse.builder()
                .orderDetailId(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .menuId(orderDetail.getMenu().getId())
                .menuName(orderDetail.getMenu().getName())
                .quantity(orderDetail.getQuantity())
                .price(orderDetail.getPrice())
                .build();
        return orderDetailResponse;
    }
}
