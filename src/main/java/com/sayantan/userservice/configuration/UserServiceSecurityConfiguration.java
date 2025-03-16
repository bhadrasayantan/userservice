package com.sayantan.userservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class UserServiceSecurityConfiguration {
    CustomJWTAuthenticationConverter customJWTAuthenticationConverter = new CustomJWTAuthenticationConverter();
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->auth.anyRequest().authenticated())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oAuth2 -> oAuth2.jwt(jwt->jwt.jwtAuthenticationConverter(customJWTAuthenticationConverter)))
                ;
        return security.build();
    }
}
