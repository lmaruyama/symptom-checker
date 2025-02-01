package com.healthly.symptomchecker.controller;

import com.healthly.symptomchecker.dto.CredentialsRequest;
import com.healthly.symptomchecker.dto.LoginResponse;
import com.healthly.symptomchecker.dto.UserRegistrationRequest;
import com.healthly.symptomchecker.entity.User;
import com.healthly.symptomchecker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    ResponseEntity<String> register(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        final User user = UserRegistrationRequest.newUser(userRegistrationRequest);
        authService.register(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    LoginResponse login(@RequestBody CredentialsRequest credentials) {
        final User user = authService.login(credentials);
        return new LoginResponse(user.getUserId());
    }
}
