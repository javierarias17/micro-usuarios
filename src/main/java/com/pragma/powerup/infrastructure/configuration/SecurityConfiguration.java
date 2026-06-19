package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.infrastructure.security.JwtAccessDeniedHandler;
import com.pragma.powerup.infrastructure.security.JwtAuthenticationEntryPoint;
import com.pragma.powerup.infrastructure.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private static final String ENDPOINT_AUTH_LOGIN = "/api/v1/auth/login";

    private static final String ENDPOINT_USER_CREATE_OWNER = "/api/v1/user/owner";
    private static final String ENDPOINT_USER_CREATE_CUSTOMER = "/api/v1/user/customer";
    private static final String ENDPOINT_USER_CREATE_EMPLOYEE = "/api/v1/user/employee";
    private static final String ENDPOINT_USER_IS_OWNER = "/api/v1/user/*/is-owner";
    private static final String ENDPOINT_USER_EMPLOYEE_RESTAURANT_ID = "/api/v1/user/employee/*/restaurant-id";

    private static final String SWAGGER_API_DOCS_PATH = "/v3/api-docs/**";
    private static final String SWAGGER_UI_PATH = "/swagger-ui/**";
    private static final String SWAGGER_HTML_PATH = "/swagger-ui.html";

    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_OWNER = "OWNER";
    private static final String ROLE_EMPLOYEE = "EMPLOYEE";

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .authorizeHttpRequests(auth -> auth
                        .antMatchers(HttpMethod.POST, ENDPOINT_AUTH_LOGIN).permitAll()
                        .antMatchers(HttpMethod.POST, ENDPOINT_USER_CREATE_CUSTOMER).permitAll()
                        .antMatchers(SWAGGER_API_DOCS_PATH, SWAGGER_UI_PATH, SWAGGER_HTML_PATH).permitAll()
                        .antMatchers(HttpMethod.GET, ENDPOINT_USER_IS_OWNER).hasRole(ROLE_ADMIN)
                        .antMatchers(HttpMethod.POST, ENDPOINT_USER_CREATE_OWNER).hasRole(ROLE_ADMIN)
                        .antMatchers(HttpMethod.POST, ENDPOINT_USER_CREATE_EMPLOYEE).hasRole(ROLE_OWNER)
                        .antMatchers(HttpMethod.GET, ENDPOINT_USER_EMPLOYEE_RESTAURANT_ID).hasRole(ROLE_EMPLOYEE)
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
