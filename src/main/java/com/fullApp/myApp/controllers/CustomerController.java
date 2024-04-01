package com.fullApp.myApp.controllers;

import com.fullApp.myApp.models.Customer;
import com.fullApp.myApp.models.CustomerRegistrationRequest;
import com.fullApp.myApp.models.CustomerUpdateRequest;
import com.fullApp.myApp.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@AllArgsConstructor
@Tag(name="Customer Controller", description="CRUD Operations with customers")
public class CustomerController {
    private final CustomerService customerService;


    @GetMapping
    @Operation(
            summary = "Get all customers",
            description = "Get all customers"
    )
    public List<Customer> allCustomers(){
        return customerService.selectAllCustomers();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get 1 customer by id"
    )
    public Customer findOne(
            @PathVariable("id") Long id
    ){
        return customerService.findCustomerById(id);
    }

    @PostMapping
    @Operation(
            summary = "Register customer"
    )
    public void registerCustomer(
            @RequestBody CustomerRegistrationRequest registrationRequest
            ){

        customerService.insertCustomer(registrationRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete customer by id"
    )
    public void deleteCustomer(
            @PathVariable("id") Long id
    ){
        customerService.deleteCustomerById(id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Allows to update customer by id"
    )
    public void updateCustomer(
            @PathVariable("id") Long id,
            @RequestBody CustomerUpdateRequest request
    ){
        customerService.updateCustomer(id, request);
    }


}
