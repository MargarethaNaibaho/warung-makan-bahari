package com.enigmacamp.springbootwmbreview.service.impl;

import com.enigmacamp.springbootwmbreview.dto.request.NewCustomerRequest;
import com.enigmacamp.springbootwmbreview.dto.request.PagingCustomerRequest;
import com.enigmacamp.springbootwmbreview.dto.response.CustomerResponse;
import com.enigmacamp.springbootwmbreview.entity.Customer;
import com.enigmacamp.springbootwmbreview.repository.CustomerRepository;
import com.enigmacamp.springbootwmbreview.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {
    @InjectMocks
    private CustomerServiceImpl customerService;

    //ini diinject karna di CustomerServiceImpl kita butuh data customerRepository dan validationUtil
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ValidationUtil validationUtil;

    @BeforeEach
    //ini dibuat supaya MockitoAnnotations..... ga diterapkan di tiap test case yg akan dibuat
    //karna ini void method setUp akan selalu di run pertama kali
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNewCustomer() {
    }

    @Test
    void getByIdCustomer() {
    }

    @Test
    void getAllCustomer_WhenCustomerFound_ShouldReturnAll() {
        //1. Arrange
        Customer customer1 = new Customer("John Martha");
        Customer customer2 = new Customer("Avel avel");
        List<Customer> customerList = Arrays.asList(customer1, customer2);

        PagingCustomerRequest pagingCustomerRequest = new PagingCustomerRequest(1, 2);
        Pageable pageable = PageRequest.of(pagingCustomerRequest.getPage() - 1, pagingCustomerRequest.getSize());
        Page<Customer> customerPage = new PageImpl<>(customerList, pageable, customerList.size());

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);

        //2. Act
        Page<Customer> result = customerService.getAllCustomer(pagingCustomerRequest);

        //3. Assertion
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("John Martha", result.getContent().get(0).getName());
        assertEquals("Avel avel", result.getContent().get(1).getName());

        //verify repository interaction
        verify(customerRepository, times(1)).findAll(pageable);

    }

    @Test
    void updateCustomer() {
    }

    @Test
    void deleteCustomerById_WhenIdIsFound_ShouldBeDeleted() {
        Customer customer = Customer.builder()
                .id("123")
                .name("Marttttttt")
                .build();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        customerService.deleteCustomerById(customer.getId());
        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    void deleteCustomerById_WhenIdNotFound_ShouldReturnException(){
        when(customerRepository.findById("124")).thenReturn(Optional.empty());

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> customerService.deleteCustomerById("124"));
        assertEquals("Customer isn't found!", runtimeException.getMessage());

        //aku buat never karna id aja blum ketemu, gimana ceritnya pulak jalankan delete() dari customerRepository
        verify(customerRepository, never()).delete(any());
    }

    @Test
    void createNewCust2_WhenValidRequest_ShouldReturnCustomerResponse() {
        //1. arrange (penyusunan)
        //preparation customer dto data
        NewCustomerRequest request = new NewCustomerRequest();
        request.setName("Martha");
        request.setPhoneNumber("080808122134");

        //preparation customer entity data
        Customer customer = Customer.builder()
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .build();

        //artinya any di sini adalah si saveAndFlush consume nilainya kemudian dibandingkan dengan hasil return
        when(customerRepository.saveAndFlush(any(Customer.class))).thenReturn(customer);

        //2. act (aksi / tindakan / perilaku)
        CustomerResponse response = customerService.createNewCust2(request);

        //3. assert (evaluasi, pemeriksaan, pencocokan)
        assertNotNull(response);
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getPhoneNumber(), response.getPhoneNumber());
    }

    @Test
    void createNewCust2_WhenInvalidRequest_ShouldThrowException(){
        //1. arrange
        NewCustomerRequest request = new NewCustomerRequest();
        request.setPhoneNumber("08080000008");
        request.setName("Martha");

        Customer customer = Customer.builder()
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .build();

        when(customerRepository.saveAndFlush(any(Customer.class))).thenThrow(new DataIntegrityViolationException("Phone number already exist"));

        //2. act
        //ResponseStatusException ini bawaan java
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> customerService.createNewCust2(new NewCustomerRequest()));

        //3. assert
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Phone number already exist", exception.getReason());
    }

    @Test
    void createNewCust3() {
    }

    @Test
    void getOne_WhenCustomerIsFound_ShouldReturnCustomer() {
        Customer customer = Customer.builder()
                .id("123124")
                .name("marthasyyyy")
                .phoneNumber("08081399493")
                .build();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        CustomerResponse response = customerService.getOne(customer.getId());

        assertNotNull(response);
        assertEquals(customer.getName(), response.getName());
        assertEquals(customer.getPhoneNumber(), response.getPhoneNumber());
        assertEquals(customer.getId(), response.getCustomerId());

        verify(customerRepository, times(1)).findById(customer.getId());
    }

    @Test
    void getOne_WhenCustomerNotFound_ShouldThrowNotFound(){
        when(customerRepository.findById("124")).thenReturn(Optional.empty());

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> customerService.getOne("124"));

        assertEquals("Customer isn't found!", runtimeException.getMessage());
        verify(customerRepository, times(1)).findById("124");
    }
}