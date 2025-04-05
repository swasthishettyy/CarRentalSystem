package com.demo.CarRentalApp.service;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demo.CarRentalApp.entity.User;
import com.demo.CarRentalApp.repository.UserRepository;



@Service
public class CustomUserDetailsService implements UserDetailsService {
	// Logger for debugging and tracking authentication flow
	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	logger.info("Authenticating user: " + username);
    	// Fetch user from the database using the username
    	User user = userRepository.findByUsername(username);
    	// If user not found, throw an exception
        if (user == null) {
        	logger.error("User not found: " + username);
            throw new UsernameNotFoundException("User not found with username: " + username);
            
        }
        logger.info("User found: " + user.getUsername() + " with role: " + user.getRole());

     // Return UserDetails object with username, encoded password, and authorities (roles)
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .build();

}
}
