package com.demo.CarRentalApp.service;

import com.demo.CarRentalApp.entity.Booking;
import com.demo.CarRentalApp.entity.Car;
import com.demo.CarRentalApp.entity.Payment;
import com.demo.CarRentalApp.entity.User;
import com.demo.CarRentalApp.repository.BookingRepository;
import com.demo.CarRentalApp.repository.PaymentRepository;
import com.demo.CarRentalApp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Enables Mockito support for JUnit 5 tests
class PdfReportServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PdfReportService pdfReportService;  // Injects mocked dependencies into PdfReportService

    private User user;
    private Booking booking;
    private Payment payment;
    
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        Car car = new Car();  //  Create a Car object
        car.setId(10L);       //  Set Car ID

        booking = new Booking();
        booking.setId(1L);
        booking.setStartDate(LocalDate.now());
        booking.setEndDate(LocalDate.now().plusDays(3));
        booking.setTotalAmount(150.0);
        booking.setCar(car);  // Attach Car object

        payment = new Payment();
        payment.setId(1L);
        payment.setAmountPaid(150.0);
        payment.setPaymentStatus("SUCCESS");
    }

    //Successfully generate PDF report for a valid user with booking and payment.
    @Test
    void testGenerateUserReport_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(bookingRepository.findByUserId(1L)).thenReturn(List.of(booking));
        when(paymentRepository.findByUserId(1L)).thenReturn(List.of(payment));

        byte[] pdfBytes = pdfReportService.generateUserReport("testUser");
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);

        verify(userRepository, times(1)).findByUsername("testUser");
        verify(bookingRepository, times(1)).findByUserId(1L);
        verify(paymentRepository, times(1)).findByUserId(1L);
    }
    //Report generation fails when user is not found.
    @Test
    void testGenerateUserReport_UserNotFound() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(null);
        
        Exception exception = assertThrows(RuntimeException.class, 
            () -> pdfReportService.generateUserReport("unknownUser"));
        
        assertEquals("User not found!", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("unknownUser");
    }
    //Report generation fails when the user has no bookings.
    @Test
    void testGenerateUserReport_NoBookingsFound() {
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(bookingRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        
        Exception exception = assertThrows(RuntimeException.class, 
            () -> pdfReportService.generateUserReport("testUser"));
        
        assertEquals("No bookings found for this user. Report cannot be generated.", exception.getMessage());
        verify(bookingRepository, times(1)).findByUserId(1L);
    }
}