package com.enigmacamp.springbootwmbreview.service.impl;

import com.enigmacamp.springbootwmbreview.dto.request.OrderDetailRequest;
import com.enigmacamp.springbootwmbreview.dto.request.OrderRequest;
import com.enigmacamp.springbootwmbreview.entity.*;
import com.enigmacamp.springbootwmbreview.repository.MenuRepository;
import com.enigmacamp.springbootwmbreview.repository.OrderDetailRepository;
import com.enigmacamp.springbootwmbreview.repository.OrderRepository;
import com.enigmacamp.springbootwmbreview.service.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    //private final OrderDetailRepository orderDetailRepository; //cross repository sangat tidak disarankan karna bisa terjadi stackoverflow. order panggil order detail, kemudian dia panggil order, kemudian order ini panggil order detail lg, dan seterusnya
    //tapi datanya tetap masuk. tapi gimana caranya supaya ga stackoverflow? pake @JsonManagedReference dan @JsonBackReference. Kita juga bisa pake @JsonIgnore pada anakan supaya nanti yg dipanggil hanya satu kali

    //supaya ga cross repo
    private final OrderDetailService orderDetailService;
    private final MenuService menuService;
    private final CustomerService customerService;
    private final TableService tableService;

    @Transactional(rollbackFor = Exception.class) // ini untuk transaction begin sampe commit atau rollback kalo ada yg gagal
    @Override
    //cara 1 -> ini maksudnya yg ga pake customer service dan tabel service. ini dah aku komentar yg ga pake service itu
//    public Order createNewTransaction(OrderRequest orderRequest) {
//        //1. buat object order
//        Order order = new Order();
//
//        //order butuh customer id. jadi buat object customer
//        //ini cara 1, rempong
////        Customer customer = new Customer();
////        customer.setId(orderRequest.getCustomerId()); //ini panggil dari dto
////        order.setCustomer(customer);
//
//        //ini cara 2, lebih clean langunsg inject dari customer service
//        Customer customer = customerService.getByIdCustomer(orderRequest.getCustomerId());
//        order.setCustomer(customer);
//
//        //order butuh tabel id. jadi buat object table dulu
////        Table table = new Table();
////        table.setId(orderRequest.getTableId()); //ini dah pake dto
////        order.setTable(table);
//
//        Table table = tableService.getTableById(orderRequest.getTableId());
//        order.setTable(table);
//
//        List<OrderDetailRequest> orderDetailRequests = orderRequest.getOrderDetails();
//        if(orderDetailRequests == null || orderDetailRequests.isEmpty()){
//            throw new IllegalArgumentException("Order details cannot be null or empty");
//        }
//
//        List<OrderDetail> orderDetails = new ArrayList<>(); //ini untuk isi order detail
//        for(OrderDetailRequest orderDetailRequest : orderDetailRequests){
//            OrderDetail orderDetail = new OrderDetail();
//
//            //order detail butuh menu id, jadi dipanggil dulu dari orderdetailrequest
//            Menu menu = menuService.getMenuById(orderDetailRequest.getMenuId());
//            menu.setId(orderDetailRequest.getMenuId());
//
//            //order detail butuh price, maanya dia panggil dari menu
//            orderDetail.setMenu(menu);
//            orderDetail.setPrice(menu.getPrice());
//            orderDetail.setQuantity(orderDetailRequest.getQuantity()); //ambil dari dto
//
//            //ini supaya list orderdetails terdata tiap order detail di dalamnya
//            orderDetails.add(orderDetail);
//        }
//
//        order.setOrderDetails(orderDetails);
//        //order butuh trans_date
//        order.setTransDate(LocalDateTime.now());
//
//        orderDetailService.createBulk(order.getOrderDetails());
//        orderRepository.saveAndFlush(order);
//
//        //ini perlu dibuat supaya di db dia order_id pada tabel t_order_detail terisi
//        for(OrderDetail orderDetail : order.getOrderDetails()){
//            orderDetail.setOrder(order);
//        }
//
//        return order;
//    }

    //cara 2
    //cara ini dia ngeflush order yg dah jadi dulu. kemudian dia ngedata order detail kemudian createBulk
    public Order createNewTransaction(OrderRequest orderRequest){
        //buat entity object terlebih dahulu
        Order order = new Order();

        //customer
        Customer customer = customerService.getByIdCustomer(orderRequest.getCustomerId());
        order.setCustomer(customer);

        //table
        Table table = tableService.getTableByName(orderRequest.getTableName());
        order.setTable(table);
        order.setTransDate(LocalDateTime.now());

        orderRepository.saveAndFlush(order);

        //order detail
        List<OrderDetail> orderDetails = new ArrayList<>();
        for(OrderDetailRequest orderDetailRequest : orderRequest.getOrderDetails()){
            //simpan order detail
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);

            //simpan menu
            Menu menu = menuService.getMenuById(orderDetailRequest.getMenuId());
            orderDetail.setMenu(menu);

            orderDetail.setQuantity(orderDetailRequest.getQuantity());
            orderDetail.setPrice(menu.getPrice());

            orderDetails.add(orderDetail);

        }

        orderDetailService.createBulk(orderDetails);
        order.setOrderDetails(orderDetails);

        return order;
    }
}
