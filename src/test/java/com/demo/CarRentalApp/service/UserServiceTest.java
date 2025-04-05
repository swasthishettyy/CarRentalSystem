package com.demo.CarRentalApp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.demo.CarRentalApp.entity.User;
import com.demo.CarRentalApp.repository.UserRepository;

@ExtendWith(MockitoExtension.class) // Enables Mockito support in JUnit 5 tests
class UserServiceTest {

    @Mock
    private UserRepository userRepository; // Mocked user repository

    @Mock
    private BCryptPasswordEncoder passwordEncoder; // Mocked password encoder

    @InjectMocks
    private UserService userService; // Injects mocks into the UserService class

    private User mockUser;
 // Set up common test data before each test method
    @BeforeEach
    void setUp() {
        mockUser = new User("testuser", "encodedPassword", User.Role.ROLE_USER);
    }
    //successful user registration when username is available.
    @Test
    void testRegisterUser_Success() {
    	// Mocking repository and encoder behavior
        when(userRepository.findByUsername("testuser")).thenReturn(null); // No user exists
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User registeredUser = userService.registerUser("testuser", "password", "ROLE_USER");

        // Assertion
        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());
        assertEquals("encodedPassword", registeredUser.getPassword());
        assertEquals(User.Role.ROLE_USER, registeredUser.getRole());
    }
    //failure of registration due to duplicate username.
    @Test
    void testRegisterUser_DuplicateUsername() {
    	// Mock that the user already exists
        when(userRepository.findByUsername("testuser")).thenReturn(mockUser); // User already exists
         // Expect a 400 BAD_REQUEST exception
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            userService.registerUser("testuser", "password", "ROLE_USER"));
        //Assertion
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("400 BAD_REQUEST \"Username is already taken!\"", exception.getMessage());
    }
    // invalid role string defaults to ROLE_USER.
    @Test
    void testRegisterUser_InvalidRole() {
    	// Mock that username is available and encoding works
        when(userRepository.findByUsername("testuser")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
     
        User registeredUser = userService.registerUser("testuser", "password", "INVALID_ROLE");

        // Assertion
        assertNotNull(registeredUser);
        assertEquals(User.Role.ROLE_USER, registeredUser.getRole(), "Should default to ROLE_USER");
    }
    //successful retrieval of a user by username
    @Test
    void testFindUserByUsername_Success() {
        // Mock user retrieval
        when(userRepository.findByUsername("testuser")).thenReturn(mockUser);
       
        User foundUser = userService.findUserByUsername("testuser");

        // Assertion
        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
    }
    //failure when searching for a non-existent user.
    @Test
    void testFindUserByUsername_NotFound() {
    	// Mock that user does not exist
        when(userRepository.findByUsername("unknownuser")).thenReturn(null);
         // Expect a 404 NOT_FOUND exception
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            userService.findUserByUsername("unknownuser"));
        //Assertion
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("404 NOT_FOUND \"User not found!\"", exception.getMessage());
    }
}
