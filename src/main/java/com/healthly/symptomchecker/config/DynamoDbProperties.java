package com.healthly.symptomchecker.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aws.dynamodb")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DynamoDbProperties {
        private String endpoint;
        private String region;
        private String accessKey;
        private String secretKey;
}
