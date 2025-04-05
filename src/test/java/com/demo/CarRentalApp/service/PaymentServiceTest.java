package com.demo.CarRentalApp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.demo.CarRentalApp.entity.Booking;
import com.demo.CarRentalApp.entity.Payment;
import com.demo.CarRentalApp.entity.User;
import com.demo.CarRentalApp.repository.BookingRepository;
import com.demo.CarRentalApp.repository.PaymentRepository;
import com.demo.CarRentalApp.repository.UserRepository;

@ExtendWith(MockitoExtension.class) // Enable Mockito with JUnit 5
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PaymentService paymentService; // PaymentService with injected mocks

    private User user;
    private Booking booking;
    private Payment payment;

    @BeforeEach
    void setUp() {
    	// Setup mock user
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        
        // Setup booking for the user
        booking = new Booking();
        booking.setId(1L);
        booking.setUser(user);
        booking.setTotalAmount(100.0);
     // Setup payment for the booking
        payment = new Payment(user, booking, 100.0, "SUCCESS", "Credit Card");
    }
    //make Successfully payment
    @Test
    void testMakePayment_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentRepository.findByUserId(user.getId())).thenReturn(Arrays.asList());
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.makePayment("testuser", 1L, 100.0, "Credit Card");

        assertNotNull(result);
        assertEquals("SUCCESS", result.getPaymentStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }
    // Payment attempt fails because user is not found
    @Test
    void testMakePayment_UserNotFound() {
        when(userRepository.findByUsername("unknown"))
                .thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> paymentService.makePayment("unknown", 1L, 100.0, "Credit Card"));
    }
    //Payment attempt fails because booking ID is invalid
    @Test
    void testMakePayment_BookingNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> paymentService.makePayment("testuser", 1L, 100.0, "Credit Card"));
    }
    // Payment fails due to insufficient amount
    @Test
    void testMakePayment_InsufficientAmount() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentRepository.findByUserId(user.getId())).thenReturn(Arrays.asList());

        assertThrows(ResponseStatusException.class, () -> paymentService.makePayment("testuser", 1L, 50.0, "Credit Card"));
    }
     //Retrieve all payments
    @Test
    void testGetAllPayments() {
        when(paymentRepository.findAll()).thenReturn(Arrays.asList(payment));

        List<Payment> result = paymentService.getAllPayments();

        assertEquals(1, result.size());
        verify(paymentRepository, times(1)).findAll();
    }
    //Retrieve all payments for a specific user
    @Test
    void testGetUserPayments_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(paymentRepository.findByUserId(user.getId())).thenReturn(Arrays.asList(payment));

        List<Payment> result = paymentService.getUserPayments("testuser");

        assertEquals(1, result.size());
        verify(paymentRepository, times(1)).findByUserId(user.getId());
    }
    //Retrieve user payments fails if user not found.
    @Test
    void testGetUserPayments_UserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> paymentService.getUserPayments("unknown"));
    }
}
