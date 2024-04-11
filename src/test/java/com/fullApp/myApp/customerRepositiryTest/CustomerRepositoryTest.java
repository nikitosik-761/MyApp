package com.fullApp.myApp.customerRepositiryTest;

import com.fullApp.myApp.AbstractTestcontainers;
import com.fullApp.myApp.models.Customer;
import com.fullApp.myApp.models.Gender;
import com.fullApp.myApp.repo.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsCustomerByEmail(){
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();

        Customer customer = Customer.builder()
                .name(FAKER.name().fullName())
                .email(email)
                .age(21)
                .gender(Gender.MALE)
                .build();

        underTest.save(customer);

        boolean actual = underTest.existsCustomerByEmail(email);

        assertThat(actual).isTrue();


    }

    @Test
    void existsCustomerByEmailWillFailWhenEmailIsNotPresent(){
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();

        boolean actual = underTest.existsCustomerByEmail(email);

        assertThat(actual).isFalse();


    }

    @Test
    void existsCustomerById(){

        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();

        Customer customer = Customer.builder()
                .name(FAKER.name().fullName())
                .email(email)
                .age(21)
                .gender(Gender.MALE)
                .build();

        underTest.save(customer);

        Long id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        boolean actual = underTest.existsCustomerById(id);

        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByIdWhenIdNotPresent(){

        Long id = -1L;

        boolean actual = underTest.existsCustomerById(id);

        assertThat(actual).isFalse();
    }


}
