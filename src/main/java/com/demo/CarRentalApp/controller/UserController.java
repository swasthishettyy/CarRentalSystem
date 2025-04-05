package com.demo.CarRentalApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.CarRentalApp.entity.User;
import com.demo.CarRentalApp.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

 // Register a new user using @RequestBody
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
    	logger.info("Registering new user: {}", user.getUsername());
    	User registeredUser = userService.registerUser(user.getUsername(), user.getPassword(), user.getRole().name());
        logger.info("User registered successfully: {}", registeredUser.getUsername());
        return registeredUser;
        
    }

    // Find user by username (for login or authentication purposes)
    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
    	logger.info("Fetching user details for username: {}", username);
        User user = userService.findUserByUsername(username);
        if (user != null) {
            logger.info("User found: {}", user.getUsername());
        } else {
            logger.warn("User not found for username: {}", username);
        }
        return user;
    }

}
