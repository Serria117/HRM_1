package com.hrm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig
{
    @Autowired
    protected AppRequestFilter requestFilter;
    private final String[] ANONYMOUS_ACCESS = {
            "/api/auth/sign-in",
            "/api/auth/sign-up",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html" //for Swagger UI
    };
    private final String[] ALLOWED_ORIGINS = {
            "http://localhost:8081",
            "http://localhost:3000",
            "http://localhost:4200",
            "http://localhost:5000",
            "https://tunganh24.github.io/"
    };
    private final String[] ALLOWED_METHODS = {"POST", "GET", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"};
    private final String[] ALLOWED_HEADERS = {"Authorization", "Cache-Control", "Content-Type", "Access-Control-Allow-Origin"};

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       PasswordEncoder passwordEncoder,
                                                       UserDetailsService userDetailsService) throws Exception
    {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                   .userDetailsService(userDetailsService)
                   .passwordEncoder(passwordEncoder)
                   .and()
                   .build();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http.httpBasic();
        http.csrf().disable();
        //config CORS in Security Filter (if not use CorsConfigurationSource as a Bean)
        http.cors(corsCustomizer -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of(ALLOWED_ORIGINS));
            configuration.setAllowedMethods(List.of(ALLOWED_METHODS));
            configuration.setAllowCredentials(true);
            //Important to pass pre-flight request by browser or will get CORS problem
            configuration.setAllowedHeaders(List.of(ALLOWED_HEADERS));
            var source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            corsCustomizer.configurationSource(source);
        });
        //config request authorization
        http.authorizeHttpRequests(auth -> {
            auth.antMatchers(ANONYMOUS_ACCESS).permitAll();
            auth.anyRequest().authenticated();
        });

        http.sessionManagement(sessionCustomizer ->
                                       sessionCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //config request filter to be used in authentication process
        http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
