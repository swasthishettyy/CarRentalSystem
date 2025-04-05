package com.demo.CarRentalApp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.demo.CarRentalApp.entity.User;
import com.demo.CarRentalApp.repository.UserRepository;

@Service
public class UserService {
	// Logger to track user service operations
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	@Autowired
    private UserRepository userRepository;
	
	// Used to encrypt user passwords securely
    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // Used to encrypt passwords

    // Register a new user
    public User registerUser(String username, String password, String role) {
        
    	logger.info("Register a new user with username: {}", username);
    	// Checking if username already exists in database
        if (userRepository.findByUsername(username) != null) {
            logger.error("Registration failed: Username {} is already taken", username);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken!");
        }
     // Encrypt the password before saving
        String encryptedPassword = passwordEncoder.encode(password);
        User.Role userRole;
        try {
            userRole = (role != null && !role.isEmpty()) ? 
                       User.Role.valueOf(role.toUpperCase()) : 
                       User.Role.ROLE_USER;
        } catch (IllegalArgumentException e) {
        	logger.warn("Invalid role provided: {}. Defaulting to ROLE_USER", role);
            userRole = User.Role.ROLE_USER; // Default to USER if invalid role provided
        }
        // Create and save user
        User user = new User( username, encryptedPassword, userRole);
        userRepository.save(user);

        logger.info("User registered successfully: {}", username);
        return user;
    }

    // Find a user by username
    public User findUserByUsername(String username) {
    	logger.info("Searching for user: {}", username);
    	 // Fetch user from the database
    	User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.error("User not found: {}", username);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }

        logger.info("User found: {}", username);
        return user;
    }

}
