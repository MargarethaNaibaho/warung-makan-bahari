package com.enigmacamp.springbootwmbreview.service.impl;

import com.enigmacamp.springbootwmbreview.entity.Menu;
import com.enigmacamp.springbootwmbreview.entity.Order;
import com.enigmacamp.springbootwmbreview.entity.OrderDetail;
import com.enigmacamp.springbootwmbreview.repository.MenuRepository;
import com.enigmacamp.springbootwmbreview.repository.OrderDetailRepository;
import com.enigmacamp.springbootwmbreview.repository.OrderRepository;
import com.enigmacamp.springbootwmbreview.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository; //cross repository sangat tidak disarankan karna bisa terjadi stackoverflow. order panggil order detail, kemudian dia panggil order, kemudian order ini panggil order detail lg, dan seterusnya
    //tapi datanya tetap masuk. tapi gimana caranya supaya ga stackoverflow? pake @JsonManagedReference dan @JsonBackReference. Kita juga bisa pake @JsonIgnore pada anakan supaya nanti yg dipanggil hanya satu kali
    private final MenuRepository menuRepository;

    @Transactional(rollbackFor = Exception.class) // ini untuk transaction begin sampe commit atau rollback kalo ada yg gagal
    @Override
    public Order createNewTransaction(Order order) {
        //1. save order detail
        orderDetailRepository.saveAllAndFlush(order.getOrderDetails());

        //2. save order
        order.setTransDate(LocalDateTime.now());
        orderRepository.saveAndFlush(order);
        //3. ngeset order
        for(OrderDetail orderDetail : order.getOrderDetails()){
            Optional<Menu> menuOptional = menuRepository.findById(orderDetail.getMenu().getId());
            if(menuOptional.isEmpty()) throw new RuntimeException("Menu Not Found");

            orderDetail.setOrder(order);
            orderDetail.setPrice(menuOptional.get().getPrice());
        }

        return order;
    }
}
