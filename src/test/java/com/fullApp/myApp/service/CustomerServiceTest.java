package com.fullApp.myApp.service;

import com.fullApp.myApp.dto.CustomerDTO;
import com.fullApp.myApp.exception.DuplicateException;
import com.fullApp.myApp.exception.ResourceNotFound;
import com.fullApp.myApp.models.Customer;
import com.fullApp.myApp.models.CustomerRegistrationRequest;
import com.fullApp.myApp.models.CustomerUpdateRequest;
import com.fullApp.myApp.models.Gender;
import com.fullApp.myApp.repo.CustomerRepository;
import com.fullApp.myApp.utils.CustomerDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final CustomerDTOMapper customerDTOMapper = new CustomerDTOMapper();


    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerRepository, passwordEncoder,customerDTOMapper);
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
                "password",
                21,
                Gender.MALE

        );

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        CustomerDTO expected = customerDTOMapper.apply(customer);

        CustomerDTO actual = underTest.findCustomerById(id);

        assertThat(actual).isEqualTo(expected);

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
        Gender gender = Gender.MALE;
        String password = "password";

        when(customerRepository.existsCustomerByEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "name",
                email,
                password,
                21,
                gender

        );

        String passwordHash = "pq309ern1";
        when(passwordEncoder.encode(request.password())).thenReturn(passwordHash);

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
        assertThat(capturedCustomer.getGender()).isEqualTo(request.gender());
        assertThat(capturedCustomer.getPassword()).isEqualTo(passwordHash);


    }

    @Test
    void insertCustomerWillThrowWhenEmailAlreadyExists(){
        String email = "email@gmail.com";
        String password = "password";
        Gender gender = Gender.MALE;

        when(customerRepository.existsCustomerByEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "name",
                email,
                password,
                21,
                gender
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
                "password",
                21,
                Gender.MALE

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
                "password",
                21,
                Gender.MALE

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
                "password",
                21,
                Gender.MALE

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
                "password",
                21,
                Gender.MALE

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
                "password",
                21,
                Gender.MALE

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
                "password",
                21,
                Gender.MALE

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