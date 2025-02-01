package com.healthly.symptomchecker.config;

import com.healthly.symptomchecker.entity.Assessment;
import com.healthly.symptomchecker.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

import static software.amazon.awssdk.regions.Region.*;

@Configuration
@AllArgsConstructor
public class DynamoDbConfiguration {

    private final DynamoDbProperties dynamoDbProperties;

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create(dynamoDbProperties.getEndpoint()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        dynamoDbProperties.getAccessKey(),
                                        dynamoDbProperties.getSecretKey())))
                .region(of(dynamoDbProperties.getRegion()))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {

        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    public DynamoDbTable<User> userTable(DynamoDbEnhancedClient dynamoDbEnhancedClient) {

        return dynamoDbEnhancedClient.table("User", TableSchema.fromBean(User.class));
    }

    @Bean
    public DynamoDbTable<Assessment> assessmentTable(
            DynamoDbEnhancedClient dynamoDbEnhancedClient) {

        return dynamoDbEnhancedClient.table("Assessment", TableSchema.fromBean(Assessment.class));
    }
}
