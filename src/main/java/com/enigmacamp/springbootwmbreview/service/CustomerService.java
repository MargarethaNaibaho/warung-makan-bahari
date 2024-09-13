package com.enigmacamp.springbootwmbreview.service;

import com.enigmacamp.springbootwmbreview.dto.request.NewCustomerRequest;
import com.enigmacamp.springbootwmbreview.dto.request.PagingCustomerRequest;
import com.enigmacamp.springbootwmbreview.dto.response.CustomerResponse;
import com.enigmacamp.springbootwmbreview.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {
    Customer createNewCustomer(Customer customer);

    CustomerResponse createNewCust2 (NewCustomerRequest newCustomerRequest);

    CustomerResponse createNewCust3(Customer customer);

    Customer getByIdCustomer(String id);

    CustomerResponse getOne(String id);

    Page<Customer> getAllCustomer(PagingCustomerRequest pagingCustomerRequest);

    Customer updateCustomer(Customer customer);

    void deleteCustomerById(String id);
}
