package com.demo.CarRentalApp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
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
import com.demo.CarRentalApp.entity.Car;
import com.demo.CarRentalApp.entity.User;
import com.demo.CarRentalApp.repository.BookingRepository;
import com.demo.CarRentalApp.repository.CarRepository;
import com.demo.CarRentalApp.repository.UserRepository;

@ExtendWith(MockitoExtension.class) // Enables Mockito support with JUnit 5
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    
    @Mock
    private CarRepository carRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks // Inject mocks into BookingService
    private BookingService bookingService;
    
    private User user;
    private Car car;
    private Booking booking;
    
    @BeforeEach
    void setUp() {
    	// Create test user
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        // Create test car
        car = new Car();
        car.setId(1L);
        car.setCarBrand("Toyota");
        car.setCarModel("Corolla");
        car.setPricePerDay(50.0);
        car.setStatus("available");
        // Create booking
        booking = new Booking(user, car, LocalDate.now(), LocalDate.now().plusDays(2), 100.0);
    }
    
    @Test
    void testBookCar_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        //call the service method
        Booking result = bookingService.bookCar(1L, 1L, LocalDate.now().toString(), LocalDate.now().plusDays(2).toString());
        //Assertion
        assertNotNull(result);
        assertEquals(100.0, result.getTotalAmount());
        verify(carRepository, times(1)).save(any(Car.class));
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }
    
    @Test
    void testBookCar_CarNotAvailable() {
        car.setStatus("unavailable"); // Car not available
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
            () -> bookingService.bookCar(1L, 1L, LocalDate.now().toString(), LocalDate.now().plusDays(2).toString()));
        //Assertion
        assertEquals("400 BAD_REQUEST \"Car is not available!\"", exception.getMessage());
    }
    
    @Test
    void testBookCar_InvalidDates() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
            () -> bookingService.bookCar(1L, 1L, LocalDate.now().plusDays(2).toString(), LocalDate.now().toString()));
        //Assertion
        assertEquals("400 BAD_REQUEST \"Start date cannot be after end date!\"", exception.getMessage());
    }
    
    @Test
    void testGetAllBookings() {
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(booking));
        List<Booking> bookings = bookingService.getAllBookings();
        //Assertion
        assertEquals(1, bookings.size());
        verify(bookingRepository, times(1)).findAll();
    }
    
    @Test
    void testGetUserBookings_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(bookingRepository.findByUserId(1L)).thenReturn(Arrays.asList(booking));
        //call service method
        List<Booking> bookings = bookingService.getUserBookings("testuser");
        //Assertion
        assertEquals(1, bookings.size());
    }
    
    @Test
    void testGetUserBookings_UserNotFound() {
        when(userRepository.findByUsername("invaliduser")).thenReturn(null);
        //call service method
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
            () -> bookingService.getUserBookings("invaliduser"));
        //Assertion
        assertEquals("404 NOT_FOUND \"User not found!\"", exception.getMessage());
    }
}
