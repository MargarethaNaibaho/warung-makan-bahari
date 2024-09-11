package com.enigmacamp.springbootwmbreview.service;

import com.enigmacamp.springbootwmbreview.dto.request.PagingCustomerRequest;
import com.enigmacamp.springbootwmbreview.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {
    Customer createNewCustomer(Customer customer);

    Customer getByIdCustomer(String id);

    Page<Customer> getAllCustomer(PagingCustomerRequest pagingCustomerRequest);

    Customer updateCustomer(Customer customer);

    void deleteCustomerById(String id);
}
