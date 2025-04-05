package com.demo.CarRentalApp.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.demo.CarRentalApp.entity.Booking;
import com.demo.CarRentalApp.entity.Car;
import com.demo.CarRentalApp.entity.User;
import com.demo.CarRentalApp.repository.BookingRepository;
import com.demo.CarRentalApp.repository.CarRepository;
import com.demo.CarRentalApp.repository.UserRepository;
//Service class that handles business logic related to booking operations.
@Service
public class BookingService {
	// Logger for logging booking-related operations
	private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    // Booking a car
    public Booking bookCar(Long userId, Long carId, String startDate, String endDate)
    {
    	logger.info("Book car with ID: {} for user ID: {}", carId, userId);
    	// Retrieving the user by ID or throw 404 if not found
    	User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", userId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
                });
    	// Retrieving the car by ID or throw 404 if not found
    	Car car = carRepository.findById(carId)
                .orElseThrow(() -> {
                    logger.error("Car not found with ID: {}", carId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found!");
                });
    	
        //Checking if car is available
        if (!"available".equalsIgnoreCase(car.getStatus())) {
        	logger.warn("Booking failed: Car with ID {} is not available!", carId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car is not available!");
        }
        //Parsing dates and validate booking period
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        if (start.isAfter(end)) {
        	logger.warn("Invalid booking dates: Start date {} is after end date {}", start, end);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date cannot be after end date!");
        }

     // Calculating number of days between start and end date
        long days = ChronoUnit.DAYS.between(start, end);
        if (days <= 0) {
        	logger.warn("Invalid booking period: Booking must be for at least one day!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking must be for at least one day!");
        }
     // Calculating total cost of the booking
        double totalPrice = days * car.getPricePerDay();
        logger.info("Booking cost calculated: {} for {} days", totalPrice, days);
        
        // Creating booking
        Booking booking = new Booking(user, car, LocalDate.parse(startDate), LocalDate.parse(endDate), totalPrice);
        bookingRepository.save(booking);
        logger.info("Booking successful: User {} booked Car {} from {} to {} for ${}", 
                user.getUsername(), car.getCarModel(), start, end, totalPrice);

        // Updates car status to "unavailable"
        car.setStatus("unavailable");
        carRepository.save(car);
        logger.info("Car status updated to unavailable for Car ID: {}", carId);
        return booking;
    }

    // Get all bookings (Admin only can access)
    public List<Booking> getAllBookings() {
    	logger.info("Fetching all bookings");
        return bookingRepository.findAll();
    }

    // Get bookings by User ID 
    public List<Booking> getUserBookings(String username) {
    	logger.info("Fetching bookings for user: {}", username);
    	// Find user by username
    	 User user = userRepository.findByUsername(username);
         if (user == null) {
        	 logger.error("User not found: {}", username);
        	 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
         }
         // Fetch bookings made by the user
         List<Booking> bookings = bookingRepository.findByUserId(user.getId());
         if (bookings.isEmpty()) {
             logger.warn("No bookings found for user: {}", username);
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No bookings found!");
         }
         logger.info("User {} has {} booking(s)", username, bookings.size());
         return bookings;
     
    }
}