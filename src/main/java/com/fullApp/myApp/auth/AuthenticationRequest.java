package com.fullApp.myApp.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
