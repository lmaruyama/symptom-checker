package com.healthly.symptomchecker.dto;

import com.healthly.symptomchecker.data.Gender;
import com.healthly.symptomchecker.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {

    private String email;
    private String password;
    private int age;
    private Gender gender;

    public static User newUser(UserRegistrationRequest request) {
        Validate.notBlank(request.getEmail(), "Email is required");
        Validate.notBlank(request.getPassword(), "Password is required");
        Validate.isTrue(request.getAge() > 0, "Age must be great than 0");

        return new User(
                request.getEmail().toLowerCase(),
                request.getPassword(),
                request.getAge(),
                request.getGender());
    }

}
