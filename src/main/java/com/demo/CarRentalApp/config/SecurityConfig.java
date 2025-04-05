package com.demo.CarRentalApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.demo.CarRentalApp.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity  // Enables Spring Security’s web security support
public class SecurityConfig {
	// Inject custom user details service
	private  CustomUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF 
            .authorizeHttpRequests(auth -> auth

                // CAR MODULE
                .requestMatchers(HttpMethod.POST, "/api/cars/add").hasAuthority("ROLE_ADMIN") 
                .requestMatchers(HttpMethod.PUT, "/api/cars/update/**").hasAuthority("ROLE_ADMIN") 
                .requestMatchers(HttpMethod.DELETE, "/api/cars/delete/**").hasAuthority("ROLE_ADMIN") 
                .requestMatchers(HttpMethod.GET, "/api/cars/all").hasAuthority("ROLE_ADMIN") 
                .requestMatchers(HttpMethod.GET, "/api/cars/available").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER") 
                .requestMatchers(HttpMethod.GET, "/api/cars/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER") 

                // BOOKING MODULE 
                .requestMatchers(HttpMethod.POST, "/api/bookings/book").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER") 
                .requestMatchers(HttpMethod.GET, "/api/bookings/all").hasAuthority("ROLE_ADMIN") 
                .requestMatchers(HttpMethod.GET, "/api/bookings/my").hasAuthority("ROLE_USER") 

                // PAYMENT MODULE 
                .requestMatchers(HttpMethod.POST, "/api/payments/make").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER") 
                .requestMatchers(HttpMethod.GET, "/api/payments/all").hasAuthority("ROLE_ADMIN") 
                .requestMatchers(HttpMethod.GET, "/api/payments/my").hasAuthority("ROLE_USER") 

                // USER REGISTRATION & LOGIN 
               
                .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll() 
                .requestMatchers(HttpMethod.GET, "/api/users/{username}").permitAll()
                
                //  PDF REPORT MODULE 
                .requestMatchers(HttpMethod.GET, "/api/reports/download").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER") 
           
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults()) // Enable Basic Authentication
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Stateless session

        return http.build(); // Return the security filter chain
    
    }
    // Used for authenticating user credentials
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    //BCrypt encoder bean used for password hashing
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

   

   
}
