package com.fullApp.myApp.service;

import com.fullApp.myApp.exception.DuplicateException;
import com.fullApp.myApp.exception.ResourceNotFound;
import com.fullApp.myApp.models.Customer;
import com.fullApp.myApp.models.CustomerRegistrationRequest;
import com.fullApp.myApp.models.CustomerUpdateRequest;
import com.fullApp.myApp.repo.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;

    public List<Customer> selectAllCustomers(){
        return repository.findAll();
    }



    public Customer findCustomerById(Long id){
        return repository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Suck!")
        );
    }

    public void insertCustomer(CustomerRegistrationRequest registrationRequest){

        if (existsPersonWithEmail(registrationRequest.email())){
            throw new DuplicateException(
                    "Customer with email: [%s] already exists!".formatted(registrationRequest.email())
            );
        }

        Customer customer = Customer.builder()
                .name(registrationRequest.name())
                .email(registrationRequest.email())
                .age(registrationRequest.age())
                                .build();


        repository.save(customer);
    }

    public void deleteCustomerById(Long id){
        if (!existsPersonWithId(id)){
            throw new ResourceNotFound("There's no customer with id [%s]".formatted(id));
        }

        repository.deleteById(id);

    }

    public boolean existsPersonWithEmail(String email){
     return repository.existsCustomerByEmail(email);
    }

    public boolean existsPersonWithId(Long id) {
        return repository.existsCustomerById(id);
    }

    public void updateCustomer(Long id, CustomerUpdateRequest requestUpdated) {
        Customer customer = repository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Not found")
        );

        boolean changed = false;

        if (requestUpdated.name() != null && !requestUpdated.name().equals(customer.getName())) {
            customer.setName(requestUpdated.name());
            changed = true;
        }


        if (requestUpdated.age() != null && !requestUpdated.age().equals(customer.getAge())) {
            customer.setAge(requestUpdated.age());
            changed = true;
        }

        if (requestUpdated.email() != null && !requestUpdated.email().equals(customer.getEmail())) {

            if (existsPersonWithEmail(requestUpdated.email())){
                throw new DuplicateException(
                        "Customer with email: [%s] already exists!".formatted(requestUpdated.email())
                );
            }

            customer.setEmail(requestUpdated.email());
            changed = true;
        }

        if (!changed){
            throw new RuntimeException("No changes were found");
        }

        repository.save(customer);




        }

    }

