package com.enigmacamp.springbootwmbreview.repository;

import com.enigmacamp.springbootwmbreview.constant.ERole;
import com.enigmacamp.springbootwmbreview.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Boolean existsCustomerByPhoneNumber(String phoneNumber);
    Optional<Customer> findByUserCredentialId(String id);
}
