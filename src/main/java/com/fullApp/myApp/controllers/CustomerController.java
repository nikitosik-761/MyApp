package com.fullApp.myApp.controllers;

import com.fullApp.myApp.dto.CustomerDTO;
import com.fullApp.myApp.jwt.JwtUtil;
import com.fullApp.myApp.models.Customer;
import com.fullApp.myApp.models.CustomerRegistrationRequest;
import com.fullApp.myApp.models.CustomerUpdateRequest;
import com.fullApp.myApp.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@AllArgsConstructor
@Tag(name="Customer Controller", description="CRUD Operations with customers")
public class CustomerController {
    private final CustomerService customerService;
    private final JwtUtil jwtUtil;


    @GetMapping
    @Operation(
            summary = "Get all customers",
            description = "Get all customers"
    )
    public List<CustomerDTO> allCustomers(){
        return customerService.selectAllCustomers();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get 1 customer by id"
    )
    public CustomerDTO findOne(
            @PathVariable("id") Long id
    ){
        return customerService.findCustomerById(id);
    }

    @PostMapping
    @Operation(
            summary = "Register customer"
    )
    public ResponseEntity<?> registerCustomer(
            @RequestBody CustomerRegistrationRequest registrationRequest
            ){

        customerService.insertCustomer(registrationRequest);
       String jwtToken = jwtUtil.issueToken(registrationRequest.email(), "ROLE_USER");

       return ResponseEntity.ok()
               .header(HttpHeaders.AUTHORIZATION, jwtToken)
               .build();
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
