package com.enigmacamp.springbootwmbreview.repository;

import com.enigmacamp.springbootwmbreview.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
}
