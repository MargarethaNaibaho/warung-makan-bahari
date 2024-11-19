package com.enigmacamp.springbootwmbreview.service.impl;

import com.enigmacamp.springbootwmbreview.dto.request.NewCustomerRequest;
import com.enigmacamp.springbootwmbreview.dto.request.PagingCustomerRequest;
import com.enigmacamp.springbootwmbreview.dto.response.CustomerResponse;
import com.enigmacamp.springbootwmbreview.entity.Customer;
import com.enigmacamp.springbootwmbreview.repository.CustomerRepository;
import com.enigmacamp.springbootwmbreview.service.CustomerService;
import com.enigmacamp.springbootwmbreview.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ValidationUtil validationUtil;

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
    public Customer getByUserCredentialCustomer(String id) {
        Optional<Customer> customer = customerRepository.findByUserCredentialId(id);
        return customer.orElseThrow(() -> new RuntimeException("Customer isn't found!"));
    }

    @Override
    public Boolean doesPhoneNumberExists(String phoneNumber){
        return customerRepository.existsCustomerByPhoneNumber(phoneNumber);
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CustomerResponse createNewCust2(NewCustomerRequest newCustomerRequest) {
        try{
            validationUtil.validate(newCustomerRequest);
            Customer customer = Customer.builder()
                    .name(newCustomerRequest.getName())
                    .phoneNumber(newCustomerRequest.getPhoneNumber())
                    .build();

            customerRepository.saveAndFlush(customer);
            return mapToCustomerResponse(customer);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exist");
        }
    }


    @Override
    public CustomerResponse createNewCust3(Customer customerReq) {
        Customer customer = customerRepository.saveAndFlush(customerReq);
        return mapToCustomerResponse(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerResponse getOne(String id) {
        Customer customer = findByIdOrThrowNotFound(id);
        return mapToCustomerResponse(customer);
    }

    private Customer findByIdOrThrowNotFound(String id){
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.orElseThrow(() -> new RuntimeException("Customer isn't found!"));
    }

    private CustomerResponse mapToCustomerResponse(Customer customer){
        return CustomerResponse.builder()
                .customerId(customer.getId())
                .name(customer.getName())
                .phoneNumber(customer.getPhoneNumber())
                .isMember(customer.getIsMember())
                .build();
    }
}
