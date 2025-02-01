package com.healthly.symptomchecker.service;

import com.healthly.symptomchecker.dto.CredentialsRequest;
import com.healthly.symptomchecker.entity.User;

public interface AuthService {
    User register(User user);
    User login(CredentialsRequest credentials);
}
