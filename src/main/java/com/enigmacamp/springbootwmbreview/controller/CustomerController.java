package com.enigmacamp.springbootwmbreview.controller;

import com.enigmacamp.springbootwmbreview.entity.Customer;
import com.enigmacamp.springbootwmbreview.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @PostMapping()
    public Customer createNewCustomer(@RequestBody Customer customer){
        return customerService.createNewCustomer(customer);
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable String id){
        return customerService.getByIdCustomer(id);
    }

    @GetMapping()
    public List<Customer> getAllCustomer(){
        return customerService.getAllCustomer();
    }

    @PutMapping()
    public Customer updateCustomer(@RequestBody(required = false) Customer customer){
        return customerService.updateCustomer(customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable String id){
        customerService.deleteCustomerById(id);
    }
}
