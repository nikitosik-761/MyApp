package com.fullApp.myApp.service;

import com.fullApp.myApp.exception.ResourceNotFound;
import com.fullApp.myApp.models.Customer;
import com.fullApp.myApp.models.CustomerRegistrationRequest;
import com.fullApp.myApp.repo.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerRepository);
    }

    @Test
    void selectAllCustomers() {
        underTest.selectAllCustomers();

        verify(customerRepository, times(1))
                .findAll();
    }

    @Test
    void findCustomerById() {
        Long id = 1L;

        Customer customer = new Customer(
                id,
                "name",
                "email",
                21

        );

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));



        Customer actual = underTest.findCustomerById(id);

        assertThat(actual).isEqualTo(customer);

    }

    @Test
    void findCustomerByIdWillThrowWhenCustomerReturnsEmptyOptional() {
        Long id = 1L;
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.findCustomerById(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("Suck!");

    }



    @Test
    void insertCustomer() {

        String email = "email@gmail.com";

        when(customerRepository.existsCustomerByEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "name",
                email,
                21
        );

        underTest.insertCustomer(request);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );

        verify(customerRepository).save(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());


    }

    @Test
    void deleteCustomer() {
    }

    @Test
    void existsPersonWithEmail() {
    }

    @Test
    void existsPersonWithId() {
    }

    @Test
    void updateCustomer() {
    }
}