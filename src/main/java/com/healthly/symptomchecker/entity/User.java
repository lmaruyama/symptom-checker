package com.healthly.symptomchecker.entity;

import com.healthly.symptomchecker.data.Gender;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import static com.healthly.symptomchecker.data.SecondaryPartitionKeys.USER_EMAIL_INDEX;

@DynamoDbBean
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Setter
public class User {

    private String userId;
    private String email;
    private String password;
    private Integer age;
    private Gender gender;

    public User(String email, String password, Integer age, Gender gender) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.gender = gender;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("user_id")
    public String getUserId() {
        return userId;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = {USER_EMAIL_INDEX})
    @DynamoDbAttribute("email")
    public String getEmail() {
        return email;
    }

    @DynamoDbAttribute("password")
    public String getPassword() {
        return password;
    }

    @DynamoDbAttribute("age")
    public Integer getAge() {
        return age;
    }

    @DynamoDbAttribute("gender")
    public Gender getGender() {
        return gender;
    }
}
