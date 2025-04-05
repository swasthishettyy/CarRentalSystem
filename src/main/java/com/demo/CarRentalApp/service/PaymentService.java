package com.demo.CarRentalApp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.demo.CarRentalApp.entity.Booking;
import com.demo.CarRentalApp.entity.Payment;
import com.demo.CarRentalApp.entity.User;
import com.demo.CarRentalApp.repository.BookingRepository;
import com.demo.CarRentalApp.repository.PaymentRepository;
import com.demo.CarRentalApp.repository.UserRepository;

@Service
public class PaymentService {
	 private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    // Make a payment
    public Payment makePayment(String username, Long bookingId, double amount, String paymentMethod) {
    	logger.info("Payment attempt by user: {} for bookingId: {}", username, bookingId);
    	User user = userRepository.findByUsername(username);
        if (user == null) {
        	logger.error("Payment failed: User not found - {}", username);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }

     //  Check if booking exists
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    logger.error("Payment failed: Booking not found - ID {}", bookingId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found!");
                });
     // Ensure user is the owner of the booking (unless admin)
        if (!booking.getUser().getUsername().equals(username) && !user.getRole().equals("ROLE_ADMIN")) {
        	logger.warn("Unauthorized payment attempt by user: {}", username);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only pay for your own bookings!");
        }

        // Prevent duplicate payments
        if (paymentRepository.findByUserId(user.getId()).stream()
                .anyMatch(p -> p.getBooking().getId().equals(bookingId))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment already made for this booking!");
        }

     // Validate payment amount
        if (amount < booking.getTotalAmount()) {
        	logger.warn("Insufficient payment amount: {} for booking ID: {}", amount, bookingId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient payment amount! Full amount required.");
        }

        // Create Payment object
        Payment payment = new Payment(user, booking, amount, "SUCCESS", paymentMethod);
        paymentRepository.save(payment);
        logger.info("Payment successful for booking ID: {} by user: {}", bookingId, username);

        return payment;
    }

    // Get all payments (Admin only)
    public List<Payment> getAllPayments() {
    	logger.info("Fetching all payments (Admin only)");
        return paymentRepository.findAll();
    }

    public List<Payment> getUserPayments(String username) {
    	 logger.info("Fetching payments for user: {}", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
        	logger.error("User not found: {}", username);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
        List<Payment> payments = paymentRepository.findByUserId(user.getId());
        logger.info("Total payments found for user {}: {}", username, payments.size());

        return payments;
    }
}