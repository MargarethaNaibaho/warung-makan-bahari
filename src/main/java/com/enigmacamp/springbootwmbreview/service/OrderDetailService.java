package com.enigmacamp.springbootwmbreview.service;

import com.enigmacamp.springbootwmbreview.dto.response.OrderDetailResponse;
import com.enigmacamp.springbootwmbreview.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> createBulk(List<OrderDetail> orderDetails); //untuk simpan banyak order detail sekaligus
    OrderDetailResponse getById(String id);
}
