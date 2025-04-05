package com.demo.CarRentalApp.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import com.demo.CarRentalApp.dto.PaymentRequest;
import com.demo.CarRentalApp.entity.Booking;
import com.demo.CarRentalApp.entity.Payment;
import com.demo.CarRentalApp.entity.User;
import com.demo.CarRentalApp.service.PaymentService;

@ExtendWith(MockitoExtension.class)  // Enables Mockito support for JUnit 5 tests
class PaymentControllerTest {

    @Mock    // Mocking the PaymentService dependency
    private PaymentService paymentService;

    @Mock   // Mocking the Authentication object
    private Authentication authentication;

    @InjectMocks   // Injects mocks into the controller
    private PaymentController paymentController;

    private Payment payment;
    private User user;
    private Booking booking;

    @BeforeEach
    void setUp() {
    	// Setup mock User
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        // Setup mock Booking
        booking = new Booking();
        booking.setId(101L);
        booking.setUser(user);

        payment = new Payment();
        payment.setId(1001L);
        payment.setBooking(booking);
        payment.setAmountPaid(500.0);
        payment.setPaymentMethod("Credit Card");
    }

    @Test  // Test making a payment
    void testMakePayment() {
        // Mock Authentication and PaymentRequest
        when(authentication.getName()).thenReturn("testuser");

        PaymentRequest request = new PaymentRequest();
        request.setBookingId(101L);
        request.setAmount(500.0);
        request.setPaymentMethod("Credit Card");

        // Mock service behavior
        when(paymentService.makePayment("testuser", 101L, 500.0, "Credit Card")).thenReturn(payment);

        // Call the controller method
        Payment result = paymentController.makePayment(authentication, request);

        // Assertion
        assertNotNull(result, "Payment result should not be null");
        assertEquals(1001L, result.getId(), "Payment ID should match");
        assertEquals(101L, result.getBooking().getId(), "Booking ID should match");
        assertEquals(500.0, result.getAmountPaid(), "Amount paid should match");
        assertEquals("Credit Card", result.getPaymentMethod(), "Payment method should match");

        // Verify service method was called once
        verify(paymentService, times(1)).makePayment("testuser", 101L, 500.0, "Credit Card");
    }

    @Test
    void testGetAllPayments() {
        // Mock multiple payments
        List<Payment> mockPayments = Arrays.asList(payment);
        when(paymentService.getAllPayments()).thenReturn(mockPayments);

        // Call the controller method
        List<Payment> result = paymentController.getAllPayments();

        // Assertion
        assertNotNull(result, "Payment list should not be null");
        assertEquals(1, result.size(), "Total payments should be 1");
        assertEquals(1001L, result.get(0).getId(), "Payment ID should match");

        // Verify service method call
        verify(paymentService, times(1)).getAllPayments();
    }

    @Test
    void testGetUserPayments() {
        // Mock Authentication and user-specific payments
        when(authentication.getName()).thenReturn("testuser");

        List<Payment> mockUserPayments = Arrays.asList(payment);
        when(paymentService.getUserPayments("testuser")).thenReturn(mockUserPayments);

        // Call the controller method
        List<Payment> result = paymentController.getUserPayments(authentication);

        // Validate response
        assertNotNull(result, "User's payment list should not be null");
        assertEquals(1, result.size(), "User should have 1 payment");
        assertEquals(1001L, result.get(0).getId(), "Payment ID should match");

        // Verify service method call
        verify(paymentService, times(1)).getUserPayments("testuser");
    }
}
