package com.fullApp.myApp.auth;

import com.fullApp.myApp.dto.CustomerDTO;
import com.fullApp.myApp.jwt.JwtUtil;
import com.fullApp.myApp.models.Customer;
import com.fullApp.myApp.utils.CustomerDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CustomerDTOMapper mapper;
    private final JwtUtil jwtUtil;

    public AuthenticationResponse login(AuthenticationRequest request){
          Authentication authentication = authenticationManager.authenticate(
                  new UsernamePasswordAuthenticationToken(
                          request.username(),
                          request.password()
                  )
          );

          Customer principal = (Customer) authentication.getPrincipal();
          CustomerDTO customerDTO = mapper.apply(principal);

          String token = jwtUtil.issueToken(customerDTO.username(), customerDTO.roles());

          return new AuthenticationResponse(token, customerDTO);

    }
}
