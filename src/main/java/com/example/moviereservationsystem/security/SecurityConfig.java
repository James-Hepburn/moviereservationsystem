package com.example.moviereservationsystem.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http.csrf (csrf -> csrf.disable ())
                .cors (Customizer.withDefaults ())
                .sessionManagement (sm -> sm.sessionCreationPolicy (SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests (auth -> auth
                .requestMatchers ("/api/auth/**").permitAll ()
                .requestMatchers (HttpMethod.GET, "/", "/login", "/register", "/movies").permitAll ()
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
                        .requestMatchers (HttpMethod.GET, "/api/movies/**").permitAll ()
                        .anyRequest ().authenticated ())
                        .authenticationProvider (authenticationProvider ())
                        .addFilterBefore (this.jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build ();
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager ();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint () {
        return new HttpStatusEntryPoint (HttpStatus.UNAUTHORIZED);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider () {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider ();
        p.setUserDetailsService (this.userDetailsService);
        p.setPasswordEncoder (passwordEncoder ());
        return p;
    }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder ();
    }
}