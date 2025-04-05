package com.demo.CarRentalApp.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.demo.CarRentalApp.dto.BookingRequest;
import com.demo.CarRentalApp.entity.Booking;
import com.demo.CarRentalApp.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
	// Logger for tracking API calls and internal status
	private static final Logger logger = LoggerFactory.getLogger(BookingController.class);


    @Autowired
    private BookingService bookingService;

    // Book a car (User/Admin)
    @PostMapping("/book")
    public Booking bookCar(@RequestBody BookingRequest request) {
    	logger.info("Booking request received for User ID: {}, Car ID: {}, Start Date: {}, End Date: {}",
                request.getUserId(), request.getCarId(), request.getStartDate(), request.getEndDate());
    	// Call the service method to process booking
    Booking booking = bookingService.bookCar(request.getUserId(), request.getCarId(), request.getStartDate(), request.getEndDate());
    logger.info("Car booked successfully! Booking ID: {}, Car ID: {}", booking.getId(), booking.getCar().getId());
    return booking;
    }

    // Get all bookings (Admin)
    @GetMapping("/all")
    public List<Booking> getAllBookings() {
    	logger.info("Fetching all bookings");
        List<Booking> bookings = bookingService.getAllBookings();
        logger.info("Total bookings retrieved: {}", bookings.size());
        return bookings;
    }

    // Get user-specific bookings
    @GetMapping("/my")
    public List<Booking> getUserBookings(Principal principal) {
    	logger.info("Fetching bookings for user: {}", principal.getName());
        List<Booking> userBookings = bookingService.getUserBookings(principal.getName());
        logger.info("Total bookings found for {}: {}", principal.getName(), userBookings.size());
        return userBookings;
    }
}