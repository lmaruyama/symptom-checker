package com.healthly.symptomchecker.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

// import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth.anyRequest().permitAll()
                )
                .cors(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(withDefaults()))
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//
//    @Bean
//    public KeyPair keyPair() {
//        try {
//            final KeyPairGenerator rsa = KeyPairGenerator.getInstance("RSA");
//            rsa.initialize(2048);
//
//            return rsa.generateKeyPair();
//        } catch (NoSuchAlgorithmException e) {
//            LOGGER.error(e.getMessage(), e);
//            throw new ApplicationSecurityException();
//        }
//    }
//
//    @Bean
//    public com.nimbusds.jose.jwk.RSAKey rsaKey(KeyPair keyPair) {
//        return new com.nimbusds.jose.jwk.RSAKey
//                .Builder((RSAPublicKey) keyPair.getPublic())
//                .privateKey(keyPair.getPrivate())
//                .keyID(UUID.randomUUID().toString())
//                .build();
//    }
//
//    @Bean
//    public JWKSource<com.nimbusds.jose.proc.SecurityContext> jwkSource(com.nimbusds.jose.jwk.RSAKey rsaKey) {
//        final JWKSet jwkSet = new JWKSet(rsaKey);
//        return (jwkSelector, context) -> jwkSelector.select(jwkSet);
//    }
//
//    @Bean
//    public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
//        return NimbusJwtDecoder
//                .withPublicKey(rsaKey.toRSAPublicKey())
//                .build();
//    }
//
//    @Bean
//    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
//        return new NimbusJwtEncoder(jwkSource);
//    }
}
