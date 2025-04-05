package com.demo.CarRentalApp.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.demo.CarRentalApp.entity.User;
import com.demo.CarRentalApp.entity.User.Role;
import com.demo.CarRentalApp.service.UserService;

@ExtendWith(MockitoExtension.class) // Enable Mockito with JUnit 5
class UserControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    @Mock
    private UserService userService; // Mocked UserService

    @InjectMocks
    private UserController userController;// Injects mocked dependencies into UserController

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        
    }

    @Test
    void testRegisterUser() {
        //  Mock user registration
        user.setRole(Role.ROLE_USER); // Ensure it matches the stored format
        when(userService.registerUser(anyString(), anyString(), anyString())).thenReturn(user);

        //Call the controller method
        User registeredUser = userController.registerUser(user);

        // Assertion
        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());
        assertEquals(Role.ROLE_USER, registeredUser.getRole(), "Role should be ROLE_USER");

        // Verify service interaction
        verify(userService, times(1)).registerUser(anyString(), anyString(), anyString());

        logger.info("Test for user registration passed.");
    }


    @Test
    void testGetUser_UserExists() {
        when(userService.findUserByUsername("testuser")).thenReturn(user);
      //Call the controller method
        User foundUser = userController.getUser("testuser");
        //Assertion
        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
        verify(userService, times(1)).findUserByUsername("testuser");
        logger.info("Test for fetching existing user passed.");
    }

    @Test
    void testGetUser_UserNotFound() {
        when(userService.findUserByUsername("unknownUser")).thenReturn(null);
      //Call the controller method
        User foundUser = userController.getUser("unknownUser");
       //Assertion
        assertNull(foundUser);
        verify(userService, times(1)).findUserByUsername("unknownUser");
        logger.warn("Test for fetching non-existing user passed.");
    }
}
