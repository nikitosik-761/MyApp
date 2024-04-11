package com.fullApp.myApp;

import com.fullApp.myApp.models.Customer;
import com.fullApp.myApp.models.Gender;
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
			int age = random.nextInt(1,100);
			Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;


			Customer customer =  Customer.builder()
					.name(faker.name().fullName())
					.email(faker.internet().safeEmailAddress())
					.age(age)
					.gender(gender)
					.build();

			repository.save(customer);


		};
	}

}
