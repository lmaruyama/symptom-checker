package com.healthly.symptomchecker.service;

import com.healthly.symptomchecker.data.Gender;
import com.healthly.symptomchecker.entity.User;
import com.healthly.symptomchecker.exception.ExistingUsernameException;
import com.healthly.symptomchecker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ImportAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
class AuthServiceTest {

    @Autowired
    AuthService authService;

    @MockitoBean
    UserRepository userRepository;

    @Test
    void createAUser_whenUserIsValid_shouldReturnTheUserWithTheId() {
        final User user =
                new User("email-test@mailnator.com", "password-test", 40, Gender.MALE);

        when(userRepository.findByEmail("email-test@mailnator.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        final User registeredUser = authService.register(user);
        assertThat(registeredUser).isNotNull();
    }

    @Test
    void createTwoUser_withTheSameEmail_shouldReturnAnExistingUsernameException() {
        final User user = new User("email-test@mailnator.com", "password-test", 40, Gender.MALE);

        when(userRepository.findByEmail("email-test@mailnator.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        final User registeredUser = authService.register(user);
        assertThat(registeredUser).isNotNull();

        final User anotherUser =
                new User("EMAIL-test@mailnator.com", "password-test-123", 40, Gender.FEMALE);

        when(userRepository.findByEmail("EMAIL-test@mailnator.com"))
                .thenReturn(Optional.of(user));

        ExistingUsernameException exception =
                assertThrows(ExistingUsernameException.class,
                        () -> authService.register(anotherUser)
                );

        String expectedMessage = "This email is already registered. Please provide another one";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).contains(expectedMessage);
    }
}
