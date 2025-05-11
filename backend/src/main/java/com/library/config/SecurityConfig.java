package com.library.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public final UserAuthProvider userAuthProvider;
    //i start by creating the security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception{
        // for simplicity, i disable the CSRF
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtAuthFilter(userAuthProvider), BasicAuthenticationFilter.class)
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) ->
                        requests
                                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/register").permitAll()
                                .requestMatchers(HttpMethod.GET, "/messages").permitAll() // 👈 allow access
                                .requestMatchers(HttpMethod.GET, "/books/search").permitAll()
                                .requestMatchers(HttpMethod.POST, "/").permitAll()
                                .anyRequest().authenticated()
                );
        return http.build();
        //i indicate that i am in a stateless application
        // a stateless application is an application that does not store any
        //info about a user's session or state between different requests
        //       Each request from a client to the server is independent and self-contained,
        //       meaning the server doesn't rely on any past interactions to fulfill a requests
        //In simpler terms:
        //Imagine you're calling a pizza place, and every time you call,
        // you have to tell them your name, address, and order from scratch—they don’t remember anything
        // from your previous call. That’s stateless.

        //=> there is no nee to handle the session
        //make the login endpoint public --- permitAll
        //and the rest of the endpoints are protected via the Authentification -- .authentificated

    }
}
