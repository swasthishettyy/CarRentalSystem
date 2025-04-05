package com.demo.CarRentalApp.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.demo.CarRentalApp.dto.PaymentRequest;
import com.demo.CarRentalApp.entity.Payment;
import com.demo.CarRentalApp.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    @Autowired
    private PaymentService paymentService;

    // User: Make a payment
    @PostMapping("/make")
    public Payment makePayment(Authentication authentication, @RequestBody PaymentRequest paymentRequest) 
    {
    	String username = authentication.getName();
    	logger.info("Payment request received from user: {} for Booking ID: {} with Amount: {} using method: {}",
                username, paymentRequest.getBookingId(), paymentRequest.getAmount(), paymentRequest.getPaymentMethod());
    
    Payment payment = paymentService.makePayment(username, paymentRequest.getBookingId(), paymentRequest.getAmount(), paymentRequest.getPaymentMethod());
    
    logger.info("Payment successful! Payment ID: {}, Booking ID: {}, Amount Paid: {}",
                payment.getId(), payment.getBooking().getId(), payment.getAmountPaid());
    
    return payment;
    }

    // Admin: Get all payments
    @GetMapping("/all")
    public List<Payment> getAllPayments() {
    	logger.info("Fetching all payments...");
        List<Payment> payments = paymentService.getAllPayments();
        logger.info("Total payments retrieved: {}", payments.size());
        return payments;
    }

    // User: Get own payment history
    @GetMapping("/my")
    public List<Payment> getUserPayments(Authentication authentication) {
        String username = authentication.getName();
        logger.info("Fetching payment history for user: {}", username);
        List<Payment> userPayments = paymentService.getUserPayments(username);
        logger.info("Total payments found for {}: {}", username, userPayments.size());
        return userPayments;
    }
}