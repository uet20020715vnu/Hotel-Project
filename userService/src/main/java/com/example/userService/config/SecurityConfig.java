package com.example.userService.config;

import com.example.userService.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        return http.getSharedObject(AuthenticationConfiguration.class).getAuthenticationManager();
//    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)  // Vô hiệu hóa CSRF
                .authorizeHttpRequests(authorizeRequests ->
                                authorizeRequests
//                                        .requestMatchers("/oauth2/user").authenticated()
//                            .requestMatchers("/api/v1/users/**").authenticated() // Yêu cầu xác thực cho /api/v1/**
                                        .requestMatchers("/api/private/**").hasRole("ADMIN")//.anyRequest().hasAnyRole("ADMIN") // Yêu cầu xác thực cho /api/private/**
//                                        .requestMatchers("/api/v1/auth/logout").authenticated()
//                            .requestMatchers("/oauth2/login-success").authenticated()
                                        .anyRequest().permitAll() // Mở quyền truy cập cho tất cả các yêu cầu khác
                )
//                .sessionManagement(sessionManagement ->
//                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin
                        )
                )
                ; // Thêm bộ lọc JWT;

        return http.build();
    }
}

