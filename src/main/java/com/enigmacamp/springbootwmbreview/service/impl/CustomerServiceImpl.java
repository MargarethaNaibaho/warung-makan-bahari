package com.enigmacamp.springbootwmbreview.service.impl;

import com.enigmacamp.springbootwmbreview.dto.request.PagingCustomerRequest;
import com.enigmacamp.springbootwmbreview.entity.Customer;
import com.enigmacamp.springbootwmbreview.repository.CustomerRepository;
import com.enigmacamp.springbootwmbreview.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    //PageRequest ini dari spring framework. Pageable juga
    public Page<Customer> getAllCustomer(PagingCustomerRequest pagingCustomerRequest) {
        Pageable pageable = PageRequest.of(pagingCustomerRequest.getPage() - 1, pagingCustomerRequest.getSize());

        //customerRepository.findAll ini mengembalikan dua jenis tipe data karna dia ada dua method di findAll.
        // Yg satu untuk iterable(list, tuple, dkk) dan satu lagi berbentuk Page
        Page<Customer> customers = customerRepository.findAll(pageable);
        return customers;
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
