package com.fullApp.myApp.models;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age,
        Gender gender

) {
}
