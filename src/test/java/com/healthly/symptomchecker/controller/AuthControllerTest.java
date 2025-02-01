package com.healthly.symptomchecker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthly.symptomchecker.config.SecurityConfiguration;
import com.healthly.symptomchecker.data.Gender;
import com.healthly.symptomchecker.dto.CredentialsRequest;
import com.healthly.symptomchecker.dto.LoginResponse;
import com.healthly.symptomchecker.entity.User;
import com.healthly.symptomchecker.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({AuthController.class})
@AutoConfigureMockMvc
@Import(SecurityConfiguration.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    AuthService authService;

    @Test
    void shouldAccessRegisterEndpoint() throws Exception {
        User user = new User("email-test@mailnator.com","password-test",38, Gender.MALE);
        when(authService.register(any(User.class))).thenReturn(user);

        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void shouldAccessLoginEndpoint() throws Exception {
        CredentialsRequest credentials = new CredentialsRequest("email-test@mailnator.com"
                ,"test123");

        User userResponse = new User();
    userResponse.setUserId("123");
        when(authService.login(credentials)).thenReturn(userResponse);

        LoginResponse response = new LoginResponse("123");

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(credentials)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }
}
