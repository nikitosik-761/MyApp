package com.fullApp.myApp.jdbc;

import com.fullApp.myApp.AbstractTestcontainers;
import com.fullApp.myApp.models.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerDataJdbcAccessServiceTest extends AbstractTestcontainers {

    private CustomerDataJdbcAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerDataJdbcAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        Customer customer = Customer.builder()
                .name(FAKER.name().fullName())
                .email(FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID())
                .age(21)
                .build();

        List<Customer> actual = underTest.selectAllCustomers();

        underTest.insertCustomer(customer);

        assertThat(actual).isNotEmpty();


    }

    @Test
    void selectCustomerById() {

        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();

        Customer customer = Customer.builder()
                .name(FAKER.name().fullName())
                .email(email)
                .age(21)
                .build();

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {

            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());


                }
        );
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById(){
        Long id = -1L;

        var actual = underTest.selectCustomerById(id);

        assertThat(actual).isEmpty();

    }

    @Test
    void insertCustomer() {
    }

    @Test
    void existsCustomerWithEmail() {
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = Customer.builder()
                .name(name)
                .email(email)
                .age(20)
                .build();

        underTest.insertCustomer(customer);

        boolean actual = underTest.existsCustomerWithEmail(email);

        assertThat(actual).isTrue();

    }

    @Test
    void existsCustomerWithEmailReturnFalseWhenDoesNotExists() {
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();

        boolean actual = underTest.existsCustomerWithEmail(email);

        assertThat(actual).isFalse();

    }

    @Test
    void existsCustomerWithId() {
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();

        Customer customer = Customer.builder()
                .name(FAKER.name().fullName())
                .email(email)
                .age(21)
                .build();

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        boolean actual = underTest.existsPersonWithId(id);

        assertThat(actual).isTrue();


    }


    @Test
    void existsCustomerWithIdWillReturnFalseWhenIdIsNotPresent(){
        Long id = 0L;

        boolean actual = underTest.existsPersonWithId(id);

        assertThat(actual).isFalse();

    }

    @Test
    void deleteCustomerById() {
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();

        Customer customer = Customer.builder()
                .name(FAKER.name().fullName())
                .email(email)
                .age(21)
                .build();

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        underTest.deleteCustomerById(id);

        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();



    }

    @Test
    void updateCustomerName() {
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();

        Customer customer = Customer.builder()
                .name(FAKER.name().fullName())
                .email(email)
                .age(21)
                .build();

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        String updatedName = "newName";

        Customer update = new Customer();
        update.setId(id);
        update.setName(updatedName);

        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(updatedName);
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());

        });

    }

    @Test
    void updateCustomerEmail() {
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();

        Customer customer = Customer.builder()
                .name(FAKER.name().fullName())
                .email(email)
                .age(21)
                .build();

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        String updatedEmail = "newEmail@gmail.com";

        Customer update = new Customer();
        update.setId(id);
        update.setEmail(updatedEmail);

        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(updatedEmail);
            assertThat(c.getAge()).isEqualTo(customer.getAge());

        });

    }

    @Test
    void updateCustomerAge() {
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();

        Customer customer = Customer.builder()
                .name(FAKER.name().fullName())
                .email(email)
                .age(21)
                .build();

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        int updatedAge = 99;

        Customer update = new Customer();
        update.setId(id);
        update.setAge(updatedAge);

        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(updatedAge);

        });

    }


    @Test
    void updateAllCustomerProperties() {
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();

        Customer customer = Customer.builder()
                .name(FAKER.name().fullName())
                .email(email)
                .age(21)
                .build();

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Customer update = new Customer();
        update.setId(id);
        update.setName("newName");
        update.setEmail(UUID.randomUUID().toString());
        update.setAge(100);

        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValue(update);

    }

    @Test
    void willNotUpdateIfNothingToUpdate(){
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();

        Customer customer = Customer.builder()
                .name(FAKER.name().fullName())
                .email(email)
                .age(21)
                .build();

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Customer update = new Customer();
        update.setId(id);

        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });


    }




}