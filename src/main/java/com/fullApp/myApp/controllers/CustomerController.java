package com.fullApp.myApp.controllers;

import com.fullApp.myApp.models.Customer;
import com.fullApp.myApp.models.CustomerRegistrationRequest;
import com.fullApp.myApp.models.CustomerUpdateRequest;
import com.fullApp.myApp.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;


    @GetMapping
    public List<Customer> allCustomers(){
        return customerService.selectAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer findOne(
            @PathVariable("id") Long id
    ){
        return customerService.findCustomerById(id);
    }

    @PostMapping
    public void registerCustomer(
            @RequestBody CustomerRegistrationRequest registrationRequest
            ){

        customerService.insertCustomer(registrationRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(
            @PathVariable("id") Long id
    ){
        customerService.deleteCustomerById(id);
    }

    @PutMapping("/{id}")
    public void updateCustomer(
            @PathVariable("id") Long id,
            @RequestBody CustomerUpdateRequest request
    ){
        customerService.updateCustomer(id, request);
    }


}
