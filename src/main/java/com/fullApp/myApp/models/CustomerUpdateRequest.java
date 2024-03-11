package com.fullApp.myApp.models;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}
