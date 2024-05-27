package com.fullApp.myApp.auth;

import com.fullApp.myApp.dto.CustomerDTO;

public record AuthenticationResponse(
        String token,
        CustomerDTO customerDTO
) {
}
