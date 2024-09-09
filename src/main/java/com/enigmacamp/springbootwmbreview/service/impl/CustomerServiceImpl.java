package com.enigmacamp.springbootwmbreview.service.impl;

import com.enigmacamp.springbootwmbreview.entity.Customer;
import com.enigmacamp.springbootwmbreview.repository.CustomerRepository;
import com.enigmacamp.springbootwmbreview.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createNewCustomer(Customer customer) {
        try {
            return customerRepository.save(customer);
        } catch(DataIntegrityViolationException e){
            throw new RuntimeException("Phone number already exist in our database!");
        }
    }

    @Override
    public Customer getByIdCustomer(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Override
    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        findByIdOrThrowNotFound(customer.getId());
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomerById(String id) {
        Customer customer = findByIdOrThrowNotFound(id);
        customerRepository.delete(customer);
    }

    private Customer findByIdOrThrowNotFound(String id){
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.orElseThrow(() -> new RuntimeException("Customer isn't found!"));
    }
}
