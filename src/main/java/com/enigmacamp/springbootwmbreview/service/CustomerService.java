package com.enigmacamp.springbootwmbreview.service;

import com.enigmacamp.springbootwmbreview.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer createNewCustomer(Customer customer);

    Customer getByIdCustomer(String id);

    List<Customer> getAllCustomer();

    Customer updateCustomer(Customer customer);

    void deleteCustomerById(String id);
}
