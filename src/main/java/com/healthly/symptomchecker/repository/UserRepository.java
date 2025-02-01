package com.healthly.symptomchecker.repository;

import com.healthly.symptomchecker.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    User save(User user);
}
