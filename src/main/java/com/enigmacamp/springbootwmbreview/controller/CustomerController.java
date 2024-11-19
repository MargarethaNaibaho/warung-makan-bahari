package com.enigmacamp.springbootwmbreview.controller;

import com.enigmacamp.springbootwmbreview.dto.request.NewCustomerRequest;
import com.enigmacamp.springbootwmbreview.dto.request.PagingCustomerRequest;
import com.enigmacamp.springbootwmbreview.dto.response.CommonResponse;
import com.enigmacamp.springbootwmbreview.dto.response.CustomerResponse;
import com.enigmacamp.springbootwmbreview.dto.response.PagingResponse;
import com.enigmacamp.springbootwmbreview.entity.Customer;
import com.enigmacamp.springbootwmbreview.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

//    @PostMapping()
//    public Customer createNewCustomer(@RequestBody Customer customer){
//        return customerService.createNewCustomer(customer);
//    }

    @PostMapping
    public ResponseEntity<?> createNewCustomer(@RequestBody NewCustomerRequest newCustomerRequest){
        CustomerResponse customerResponse = customerService.createNewCust2(newCustomerRequest);
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .message("Successfully create new customer")
                .statusCode(HttpStatus.CREATED.value())
                .data(customerResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

//    @GetMapping("/{id}")
//    public Customer getCustomerById(@PathVariable String id){
//        return customerService.getByIdCustomer(id);
//    }

    @GetMapping("/{id}")
//    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getCustomerById(@PathVariable String id){
        CustomerResponse customerResponse = customerService.getOne(id);
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .message("Successfully get customer by id")
                .statusCode(HttpStatus.OK.value())
                .data(customerResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping()
    @SecurityRequirement(name = "Bearer Authentication")
    //dari sisi user kita buat mulai dari angka 1 untuk halaman pertama
    //tapi dari sisi backend, itu dimulai dari angka 0 yg menjadi halaman pertama.makanya nanti di service, nilai dari page yg dibawa dari sini akan dikurangi 1
    public ResponseEntity<?> getAllCustomer(@RequestParam(required = false, defaultValue = "1") Integer page,
                                            @RequestParam(required = false, defaultValue = "5") Integer size){
        //nampung nilai yang diterima dari param ke pagingcustomerrequest
        //ini akan jadi value yg akan diparse ke customerService
        PagingCustomerRequest pagingCustomerRequest = PagingCustomerRequest.builder()
                .page(page)
                .size(size)
                .build();

        //nampung nilai yang diterima dari customerService
        Page<Customer> customers = customerService.getAllCustomer(pagingCustomerRequest);

        //set paging response untuk dimasukkan ke common response nanti
        //ini gunanya untuk memfilter data dari var customers karna kita cuma butuh beberapa key dari dalamnya. ga semua
        PagingResponse pagingResponse = PagingResponse.builder()
                .page(page)
                .size(size)
                .count(customers.getTotalElements())
                .totalPages(customers.getTotalPages())
                .build();

        //masukkan nilai yg tadi dah ditampung sebelumnya ke dalam common response
        //karna common response yang akan jadi body pada ResponseEntity
        //kenapa tipe datanya si CommonResponse ituu jadi <List<Customer>>?
        //karna CommonResponse ada inisialisasi tipe data generic <T> untuk dipake nanti oleh si atribut data.
        //atribut data ini untuk saat ini akan kita isi dengan list customer
        CommonResponse<List<Customer>> response = CommonResponse.<List<Customer>>builder()
                .message("Successfully get all customers")
                .statusCode(HttpStatus.OK.value())

                //getContent ini dippake untuk nampung datanyanya tadi yg masih dalam bentuk page
                //menjadi List. makanya tipe data
                .data(customers.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
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
