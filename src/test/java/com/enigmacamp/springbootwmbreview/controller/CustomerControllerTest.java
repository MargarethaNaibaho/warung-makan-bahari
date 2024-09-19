package com.enigmacamp.springbootwmbreview.controller;

import com.enigmacamp.springbootwmbreview.dto.request.NewCustomerRequest;
import com.enigmacamp.springbootwmbreview.dto.response.CustomerResponse;
import com.enigmacamp.springbootwmbreview.service.CustomerService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//WebMvcTest supaya buat untuk tes unit pada komponen web. Ini fokus tes pada controller dan konfigurasi konteks Spring yang memuat hanya komponen terkait web
@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {
    @MockBean
    private CustomerService customerService;

    //MockMvc gunanya supaya bisa buat permintaan HTTP ke controller dan memeriksa respon tanpa memulai server web sebenarnya
    @Autowired
    private MockMvc mockMvc;

    //ObjectMapper untuk serialisasi (mengubah objek java menjad JSON) dan deserialisasi (mengubah JSON menjadi objek Java)
    @Autowired
    private ObjectMapper objectMapper;

//    @BeforeEach
//    void setUp(){
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
//                .build();
//
//        objectMapper = new ObjectMapper();
//    }

    @WithMockUser
    @Test
    void createNewCustomer_ShouldReturnCreatedCustomer() throws Exception {
        //Arrange
        NewCustomerRequest request = new NewCustomerRequest();
        request.setName("Marrthaaa");
        request.setPhoneNumber("0810821034824");

        CustomerResponse customerResponse = CustomerResponse.builder()
                .customerId("123124")
                .name("Marrthaaa")
                .phoneNumber("0810821034824")
                .build();

        when(customerService.createNewCust2(any(NewCustomerRequest.class))).thenReturn(customerResponse);

        //act and assert
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.data.name").value("Marrthaaa")) //.data ini maksudnya karna kita pake dto
                        .andExpect(jsonPath("$.data.phoneNumber").value("0810821034824"));

        verify(customerService, times(1)).createNewCust2(any(NewCustomerRequest.class));
    }

    @WithMockUser
    @Test
    void getCustomerById_WhenCustomerIsFound_ShouldReturnCustomer() throws Exception{
        CustomerResponse customerResponse = CustomerResponse.builder()
                .customerId("123124")
                .name("Marrthaaa")
                .phoneNumber("0810821034824")
                .build();
        when(customerService.getOne(customerResponse.getCustomerId())).thenReturn(customerResponse);

        //act and assert
        mockMvc.perform(get("/api/customers/123124")
                .contentType(MediaType.APPLICATION_JSON))
//                .content(objectMapper.writeValueAsString("123124"))) //ini gausah dipake di get karna itu cuma dipake di post aja
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Marrthaaa")) //.data ini maksudnya karna kita pake dto
                .andExpect(jsonPath("$.data.phoneNumber").value("0810821034824"));

        verify(customerService, times(1)).getOne(customerResponse.getCustomerId());

    }

    @WithMockUser
    @Test
    void getCustomerById_WhenCustomerNotFound_ShouldThrowException() throws Exception{

        when(customerService.getOne("1234")).thenThrow(new RuntimeException("Customer isn't found!"));

        //act and assert
        mockMvc.perform(get("/api/customers/123124")
                        .contentType(MediaType.APPLICATION_JSON))
//                .content(objectMapper.writeValueAsString("123124"))) //ini gausah dipake di get karna itu cuma dipake di post aja
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Customer isn't found!"));

        verify(customerService, times(1)).getOne("1234");

    }

    @Test
    void getAllCustomer() {
    }

    @Test
    void updateCustomer() {
    }

    @Test
    void deleteCustomer() {
    }
}