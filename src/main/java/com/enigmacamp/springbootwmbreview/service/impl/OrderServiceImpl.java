package com.enigmacamp.springbootwmbreview.service.impl;

import com.enigmacamp.springbootwmbreview.dto.request.OrderDetailRequest;
import com.enigmacamp.springbootwmbreview.dto.request.OrderRequest;
import com.enigmacamp.springbootwmbreview.dto.response.OrderDetailResponse;
import com.enigmacamp.springbootwmbreview.dto.response.OrderResponse;
import com.enigmacamp.springbootwmbreview.entity.*;
import com.enigmacamp.springbootwmbreview.repository.OrderRepository;
import com.enigmacamp.springbootwmbreview.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public OrderResponse createNewTransaction(OrderRequest orderRequest){
        Customer customer = customerService.getByIdCustomer(orderRequest.getCustomerId());
        Table table = tableService.getTableByName(orderRequest.getTableName());

        //buat entity object terlebih dahulu
        //ini pake chaining method. nnti kalo ada method yg dipanggil berulang kali, nilainya yg dipake adalah method itu yg terakhir
        //ga perlu urut dengan definisi dari kelas si order itu
        Order order = Order.builder()
                .customer(customer)
                .table(table)
                .transDate(LocalDateTime.now())
                .build();

        orderRepository.saveAndFlush(order);

        //order detail
        List<OrderDetail> orderDetails = new ArrayList<>();
        for(OrderDetailRequest orderDetailRequest : orderRequest.getOrderDetails()){
            Menu menu = menuService.getMenuById(orderDetailRequest.getMenuId());
            //simpan order detail
            //pake builder ini kek pake setter gitu
            OrderDetail orderDetail = OrderDetail.builder()
                    .price(menu.getPrice())
                    .order(order)
                    .quantity(orderDetailRequest.getQuantity())
                    .menu(menu)
                    .build();

            orderDetails.add(orderDetail);

        }

        orderDetailService.createBulk(orderDetails);
        order.setOrderDetails(orderDetails);

        return mapToOrderResponse(order);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderDetailResponse> orderDetailResponses =  order.getOrderDetails().stream()
                .map(
                    orderDetail -> {
                        return OrderDetailResponse.builder()
                                .orderDetailId(orderDetail.getId())
                                .orderId(orderDetail.getOrder().getId())
                                .menuId(orderDetail.getMenu().getId())
                                .menuName(orderDetail.getMenu().getName())
                                .price(orderDetail.getPrice())
                                .quantity(orderDetail.getQuantity())
                                .build();
                    }
                )
                .collect(Collectors.toList());

        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .customerId(order.getCustomer().getId())
                .tableName(order.getTable().getName())
                .orderDetails(orderDetailResponses)
                .transDate(order.getTransDate())
                .build();
        return orderResponse;
    }

    @Override
    public List<OrderResponse> getAll() {
        List<Order> orders = orderRepository.findAll();

        //cara pertama
        List<OrderResponse> orderResponses = new ArrayList<>();
        for(Order order : orders){
            OrderResponse orderResponse = mapToOrderResponse(order);
            orderResponses.add(orderResponse);
        }

        //cara kedua pake lambda stream
        List<OrderResponse> orderResponseListStream = orders.stream().map(order -> mapToOrderResponse(order)).collect(Collectors.toList());

        return orderResponses;
    }

    @Override
    public OrderResponse getById(String id) {
        OrderResponse orderResponse = mapToOrderResponse(orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order isnt found!")));
        return orderResponse;
    }
}
