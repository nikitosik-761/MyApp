package com.fullApp.myApp.models;

public record CustomerRegistrationRequest(
        String name,
        String email,

        String password,
        Integer age,
        Gender gender

) {
}
