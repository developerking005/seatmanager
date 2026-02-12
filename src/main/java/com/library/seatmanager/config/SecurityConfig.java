package com.library.seatmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/login.html",
//                                "/otp.html",
//
//                                // login & otp assets
//                                "/login.css",
//                                "/login.js",
//                                "/otp.css",
//                                "/otp.js",
//
//                                // shared assets
//                                "/header.html",
//                                "/header.js",
//
//                                // dashboard assets
//                                "/dashboard.css",
//                                "/dashboard.js",
//
//                                // profile assets (THIS WAS MISSING)
//                                "/profile.js",
//
//                                // misc
//                                "/favicon.ico",
//
//                                "/index.html",
//                                "/index.css",
//
//                                "/new-index.html",
//                                "/new-index.css",
//                                "/new-index.js",
//
//                                "/signup.html",
//                                "/signup.js",
//
//                                "/login.html",
//                                "/login.js",
//
//                                "/library.html",
//                                "/create-library.js",
//
//                                // auth APIs
//                                "/api/auth/**"
//                        ).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form.disable())
//                .logout(logout -> logout
//                        .logoutUrl("/api/auth/logout")
//                        .logoutSuccessUrl("/login.html")
//                );
//        return http.build();
//    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/login.html",
                                "/newindex.html",
                                "/newlogin.html",
                                "/newsignup.html",
                                "/createlibrary.html",
                                "/signup.html",
                                "/otp.html",
                                "/dashboard.html",
                                "/library.html",

                                "/css/**",
                                "/js/**",
                                "/images/**",

                                "/api/auth/**",
                                "/api/student/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable())
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessUrl("/login.html")
                );

        return http.build();
    }

@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
}