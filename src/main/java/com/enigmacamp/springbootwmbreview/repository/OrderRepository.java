package com.enigmacamp.springbootwmbreview.repository;

import com.enigmacamp.springbootwmbreview.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    //di sini ga boleh ada crud untuk delete. hanya ada getbyid, getall
    List<Order> findAllByCustomer_IdOrderByTransDateDesc(String id);
}
