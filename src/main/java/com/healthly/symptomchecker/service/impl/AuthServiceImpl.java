package com.healthly.symptomchecker.service.impl;

import com.healthly.symptomchecker.dto.CredentialsRequest;
import com.healthly.symptomchecker.entity.User;
import com.healthly.symptomchecker.exception.ExistingUsernameException;
import com.healthly.symptomchecker.exception.InvalidCredentialsException;
import com.healthly.symptomchecker.repository.UserRepository;
import com.healthly.symptomchecker.service.AuthService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(User user) {
        final Optional<User> userByEmail = findUserByEmail(user.getEmail());
        userByEmail.ifPresent(existingUser -> {
            throw new ExistingUsernameException();
        });
        user.setUserId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User login(CredentialsRequest credentials) {
        if (credentials == null) {
            log.info("Attempt to log in without providing user credentials");
            throw new InvalidCredentialsException();
        }

        Optional<User> userOptional = findUserByEmail(credentials.email());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(credentials.password(), user.getPassword())) {
                return user;
            }
        }
        log.info("Invalid credentials for {}", credentials.email());
        throw new InvalidCredentialsException();
    }

    private Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
