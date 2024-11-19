package com.enigmacamp.springbootwmbreview.service.impl;

import com.enigmacamp.springbootwmbreview.dto.request.OrderDetailRequest;
import com.enigmacamp.springbootwmbreview.dto.request.OrderRequest;
import com.enigmacamp.springbootwmbreview.dto.response.OrderDetailResponse;
import com.enigmacamp.springbootwmbreview.dto.response.OrderResponse;
import com.enigmacamp.springbootwmbreview.entity.*;
import com.enigmacamp.springbootwmbreview.repository.OrderRepository;
import com.enigmacamp.springbootwmbreview.service.CustomerService;
import com.enigmacamp.springbootwmbreview.service.MenuService;
import com.enigmacamp.springbootwmbreview.service.OrderDetailService;
import com.enigmacamp.springbootwmbreview.service.TableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {
    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailService orderDetailService;

    @Mock
    private MenuService menuService;

    @Mock
    private CustomerService customerService;

    @Mock
    private TableService tableService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNewTransaction_Succeed() {
        OrderRequest request = new OrderRequest();
        request.setCustomerId("123");
        request.setTableId("Table A");

        //set up mock customer
        Customer customer = Customer.builder()
                .id("123")
                .name("soleh")
                .build();

        when(customerService.getByIdCustomer(customer.getId())).thenReturn(customer);

        //set up mock table
        Table table = Table.builder()
                .name("Table A")
                .build();

        when(tableService.getTableByName(table.getName())).thenReturn(table);

        //set mock for order
        Order order = Order.builder()
                .customer(customer)
                .table(table)
                .transDate(LocalDateTime.now())
                .build();

        when(orderRepository.saveAndFlush(any(Order.class))).thenReturn(order);

        Menu menu = Menu.builder()
                .id("1")
                .price(10000L)
                .build();

        when(menuService.getMenuById("1")).thenReturn(menu);

        //setup order detail
        OrderDetailRequest orderDetailRequest = new OrderDetailRequest();
        orderDetailRequest.setMenuId("1");
        orderDetailRequest.setQuantity(2);
        request.setOrderDetails(Collections.singletonList(orderDetailRequest));

        List<OrderDetail> orderDetails = new ArrayList<>();
        OrderDetail orderDetail = OrderDetail.builder()
                .id("445")
                .quantity(2)
                .price(10000L)
                .menu(menu)
                .build();
        orderDetails.add(orderDetail);

        when(orderDetailService.createBulk(anyList())).thenReturn(orderDetails);

        OrderResponse response = orderService.createNewTransaction(request);

        assertNotNull(response);
    }

    @Test
    void getAll_WhenThereIsAnOrder_ShouldReturnOrder() {
        //set up mock customer
        Customer customer = Customer.builder()
                .id("123")
                .name("soleh")
                .build();

//        when(customerService.getByIdCustomer(customer.getId())).thenReturn(customer);

        //set up mock table
        Table table = Table.builder()
                .name("Table A")
                .build();

//        when(tableService.getTableByName(table.getName())).thenReturn(table);

        Menu menu = Menu.builder()
                .id("1")
                .price(10000L)
                .build();

//        when(menuService.getMenuById("1")).thenReturn(menu);

        List<OrderDetail> orderDetails = new ArrayList<>();

        //set mock for order
        Order order = Order.builder()
                .id("1111")
                .customer(customer)
                .table(table)
                .transDate(LocalDateTime.now())
//                .orderDetails(orderDetails)
                .build();


        OrderDetail orderDetail = OrderDetail.builder()
                .id("445")
                .quantity(2)
                .price(10000L)
                .menu(menu)
                .order(order)
                .build();
        orderDetails.add(orderDetail);
        order.setOrderDetails(orderDetails);

        //when repo ini dipake untuk tiru orderRepository asli yg berhubungan dengan db
        //jadi, when ini harus dipanggil terlebih dahulu suupaya si service bisa dipanggil
        //karna dapat kode aslinya, si service akan panggil repo terlebih dahulu untuk memperoleh data
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

        List<OrderResponse> orderResponses = orderService.getAll();

        //verifikasi kalo .findAll dijalankan 1x
        verify(orderRepository, times(1)).findAll();

        //verifikasi orderResponses ga bernilai null
        assertNotNull(orderResponses);

        //verifikasi jumlah order yg ada hanya 1
        assertEquals(1, orderResponses.size());

        OrderResponse response = orderResponses.get(0);

        //verifikasi order
        assertEquals(order.getId(), response.getOrderId());
        assertEquals(customer.getId(), response.getCustomerId());
        assertEquals(table.getName(), response.getTableName());
        assertEquals(order.getTransDate(), response.getTransDate());
        assertEquals(1, response.getOrderDetails().size());

        //verifikasi order detail
        OrderDetailResponse orderDetailResponse = response.getOrderDetails().get(0);
        assertEquals(orderDetail.getId(), orderDetailResponse.getOrderDetailId());
        assertEquals(menu.getId(), orderDetailResponse.getMenuId());
        assertEquals(orderDetail.getPrice(), orderDetailResponse.getPrice());
        assertEquals(orderDetail.getQuantity(), orderDetailResponse.getQuantity());
    }

    @Test
    void getAll_WhenNoOrder_ShouldReturnException(){
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> orderService.getAll());

        verify(orderRepository, times(1)).findAll();
        assertEquals("No orders found!", runtimeException.getMessage());
    }

    @Test
    void getById_WhenOrderIdIsFound_ShouldReturnOrder() {
        //set up mock customer
        Customer customer = Customer.builder()
                .id("123")
                .name("soleh")
                .build();

//        when(customerService.getByIdCustomer(customer.getId())).thenReturn(customer);

        //set up mock table
        Table table = Table.builder()
                .name("Table A")
                .build();

//        when(tableService.getTableByName(table.getName())).thenReturn(table);

        Menu menu = Menu.builder()
                .id("1")
                .price(10000L)
                .build();

//        when(menuService.getMenuById("1")).thenReturn(menu);

        List<OrderDetail> orderDetails = new ArrayList<>();

        //set mock for order
        Order order = Order.builder()
                .id("1111")
                .customer(customer)
                .table(table)
                .transDate(LocalDateTime.now())
                .orderDetails(orderDetails)
                .build();


        OrderDetail orderDetail = OrderDetail.builder()
                .id("445")
                .quantity(2)
                .price(10000L)
                .menu(menu)
                .order(order)
                .build();
        orderDetails.add(orderDetail);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        OrderResponse orderResponse = orderService.getById(order.getId());

        assertEquals(order.getId(), orderResponse.getOrderId());
        assertEquals(order.getCustomer().getId(), orderResponse.getCustomerId());
        assertEquals(order.getTable().getName(), orderResponse.getTableName());
        assertEquals(order.getTransDate(), orderResponse.getTransDate());
        assertEquals(1, orderResponse.getOrderDetails().size());

        OrderDetailResponse orderDetailResponse = orderResponse.getOrderDetails().get(0);
        assertEquals(orderDetail.getId(), orderDetailResponse.getOrderDetailId());
        assertEquals(menu.getId(), orderDetailResponse.getMenuId());
        assertEquals(orderDetail.getPrice(), orderDetailResponse.getPrice());
        assertEquals(orderDetail.getQuantity(), orderDetailResponse.getQuantity());

        verify(orderRepository, times(1)).findById(order.getId());
    }

    @Test
    void getById_WhenOrderIdNotFound_ShouldReturnException(){
        when(orderRepository.findById("12444")).thenReturn(Optional.empty());

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> orderService.getById("12444"));

        assertEquals("Order isnt found!", runtimeException.getMessage());
        verify(orderRepository, times(1)).findById("12444");
    }
}