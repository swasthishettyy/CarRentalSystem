package com.demo.CarRentalApp.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.demo.CarRentalApp.dto.BookingRequest;
import com.demo.CarRentalApp.entity.Booking;
import com.demo.CarRentalApp.entity.Car;
import com.demo.CarRentalApp.entity.User;
import com.demo.CarRentalApp.service.BookingService;

@ExtendWith(MockitoExtension.class) // Enables Mockito annotations
class BookingControllerTest {

    @Mock // Mocked service layer
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private Booking booking;
    private User user;
    private Car car;

    @BeforeEach
    void setUp() {
    	// Initialize mock User
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
     // Initialize mock Car
        car = new Car();
        car.setId(101L);
        car.setCarModel("Tesla Model S");
     // Initialize mock Booking
        booking = new Booking();
        booking.setId(1001L);
        booking.setUser(user);
        booking.setCar(car);
    }

    @Test
    void testBookCar() {
        //  Creating a BookingRequest DTO
        BookingRequest request = new BookingRequest();
        request.setUserId(1L);
        request.setCarId(101L);
        request.setStartDate("2025-04-10");
        request.setEndDate("2025-04-15");

        // Mock service behavior
        when(bookingService.bookCar(1L, 101L, "2025-04-10", "2025-04-15")).thenReturn(booking);

        // Calls the controller method
        Booking result = bookingController.bookCar(request);

        // Assert the Validate response
        assertNotNull(result, "Booking result should not be null");
        assertEquals(1001L, result.getId(), "Booking ID should match");
        assertEquals(1L, result.getUser().getId(), "User ID should match");
        assertEquals(101L, result.getCar().getId(), "Car ID should match");

        // Verify that service method was called once
        verify(bookingService, times(1)).bookCar(1L, 101L, "2025-04-10", "2025-04-15");
    }

    @Test
    void testGetAllBookings() {
        //  Mock multiple bookings
        List<Booking> mockBookings = Arrays.asList(booking);
        when(bookingService.getAllBookings()).thenReturn(mockBookings);

        // Call the controller method
        List<Booking> result = bookingController.getAllBookings();

        // Asserting  Validate response
        assertNotNull(result, "Booking list should not be null");
        assertEquals(1, result.size(), "Total bookings should be 1");
        assertEquals(1001L, result.get(0).getId(), "First booking ID should match");

        // Verify service method call
        verify(bookingService, times(1)).getAllBookings();
    }

    @Test
    void testGetUserBookings() {
        //  Mock Principal and user-specific bookings
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("testuser");

        List<Booking> mockUserBookings = Arrays.asList(booking);
        when(bookingService.getUserBookings("testuser")).thenReturn(mockUserBookings);

        //  Call the controller method
        List<Booking> result = bookingController.getUserBookings(principal);

        // Assert the Validate response
        assertNotNull(result, "User's booking list should not be null");
        assertEquals(1, result.size(), "User should have 1 booking");
        assertEquals(1001L, result.get(0).getId(), "Booking ID should match");

        // Verify service method call
        verify(bookingService, times(1)).getUserBookings("testuser");
    }
}
