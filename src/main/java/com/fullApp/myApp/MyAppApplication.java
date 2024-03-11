package com.fullApp.myApp;

import com.fullApp.myApp.models.Customer;
import com.fullApp.myApp.repo.CustomerRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class MyAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyAppApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(CustomerRepository repository){
		return args -> {
			Faker faker = new Faker();
			Random random = new Random();


			Customer customer =  Customer.builder()
					.name(faker.name().fullName())
					.email(faker.internet().safeEmailAddress())
					.age(random.nextInt(100))
					.build();

			repository.save(customer);


		};
	}

}
