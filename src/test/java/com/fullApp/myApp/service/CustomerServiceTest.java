package com.fullApp.myApp.service;

import com.fullApp.myApp.exception.DuplicateException;
import com.fullApp.myApp.exception.ResourceNotFound;
import com.fullApp.myApp.models.Customer;
import com.fullApp.myApp.models.CustomerRegistrationRequest;
import com.fullApp.myApp.models.CustomerUpdateRequest;
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
    void insertCustomerWillThrowWhenEmailAlreadyExists(){
        String email = "email@gmail.com";

        when(customerRepository.existsCustomerByEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "name",
                email,
                21
        );


        assertThatThrownBy(() -> underTest.insertCustomer(request))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("Customer with email: [%s] already exists!".formatted(request.email()));

        verify(customerRepository, never()).save(any());

    }

    @Test
    void deleteCustomerById() {
        Long id = 10L;
        when(customerRepository.existsCustomerById(id)).thenReturn(true);

        underTest.deleteCustomerById(id);

        verify(customerRepository).deleteById(id);

    }

    @Test
    void deleteCustomerByIdWillThrowWhenNotFound() {
        Long id = 10L;
        when(customerRepository.existsCustomerById(id)).thenReturn(false);

        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFound.class)
                        .hasMessage("There's no customer with id [%s]".formatted(id));

        verify(customerRepository, never()).deleteById(id);

    }
    @Test
    void updateCustomerAllProperties() {

        Long id = 1L;

        Customer customer = new Customer(
                id,
                "name",
                "email",
                21

        );

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        String newEmail = "newEmail@gmail.com";

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "NewName", newEmail , 23
        );

        when(customerRepository.existsCustomerByEmail(newEmail)).thenReturn(false);

        underTest.updateCustomer(id, request);




        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());

    }

    @Test
    void updateCustomerOnlyName() {

        Long id = 1L;

        Customer customer = new Customer(
                id,
                "name",
                "email",
                21

        );

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));


        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "NewName", null , null
        );

        underTest.updateCustomer(id, request);

        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());

    }

    @Test
    void updateCustomerOnlyEmail() {
        Long id = 1L;

        Customer customer = new Customer(
                id,
                "name",
                "email",
                21

        );

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        String newEmail = "newEmail@gmail.com";

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                null, newEmail , null
        );

        when(customerRepository.existsCustomerByEmail(newEmail)).thenReturn(false);

        underTest.updateCustomer(id, request);


        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());

    }

    @Test
    void updateCustomerOnlyAge() {

        Long id = 1L;

        Customer customer = new Customer(
                id,
                "name",
                "email",
                21

        );

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));


        CustomerUpdateRequest request = new CustomerUpdateRequest(
                null, null , 40
        );

        underTest.updateCustomer(id, request);

        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());

    }

    @Test
    void updateCustomerWillThrowWhenEmailHasDuplicate() {
        Long id = 1L;

        Customer customer = new Customer(
                id,
                "name",
                "email",
                21

        );

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        String newEmail = "newEmail@gmail.com";

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                null, newEmail , null
        );

        when(customerRepository.existsCustomerByEmail(newEmail)).thenReturn(true);

        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("Customer with email: [%s] already exists!".formatted(request.email()));


        verify(customerRepository, never()).save(any());


    }

    @Test
    void willThrowWhenCustomerUpdateNoChanges() {

        Long id = 1L;

        Customer customer = new Customer(
                id,
                "name",
                "email",
                21

        );

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));


        CustomerUpdateRequest request = new CustomerUpdateRequest(
                customer.getName(), customer.getEmail() , customer.getAge()
        );


        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No changes were found");


        verify(customerRepository, never()).save(any());

    }

    @Test
    void updateCustomerWillThrowWhenCustomerIsNotFound() {

        Long id = -1L;

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.updateCustomer(id, any()))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("Not found");

        verify(customerRepository, never()).save(any());

    }



}