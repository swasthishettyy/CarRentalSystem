package com.demo.CarRentalApp.integration;

import com.demo.CarRentalApp.dto.BookingRequest;
import com.demo.CarRentalApp.dto.PaymentRequest;
import com.demo.CarRentalApp.entity.Booking;
import com.demo.CarRentalApp.entity.Car;
import com.demo.CarRentalApp.entity.User;
import com.demo.CarRentalApp.repository.BookingRepository;
import com.demo.CarRentalApp.repository.CarRepository;
import com.demo.CarRentalApp.repository.PaymentRepository;
import com.demo.CarRentalApp.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Optional;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional  // Ensuring test data is rolled back after each test
class CarRentalIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CarRepository carRepository;
    
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;


    private Car car;
    
    private User user;


    @BeforeEach
    void setUp() {
        car = new Car();
        car.setCarModel("Toyota Corolla");
        car.setCarBrand("Toyota");
        car.setPricePerDay(50.0);
        car = carRepository.save(car);
        
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password"); 
        user.setRole(User.Role.ROLE_USER);
        user = userRepository.save(user);
        
        }

    // CAR MODULE TEST
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateCar_Success() throws Exception {
        car.setPricePerDay(60.0);

        mockMvc.perform(put("/api/cars/update/{id}", car.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pricePerDay").value(60.0));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteCar_Success() throws Exception {
        mockMvc.perform(delete("/api/cars/delete/{id}", car.getId()))
                .andExpect(status().isOk());

        Optional<Car> deletedCar = carRepository.findById(car.getId());
        assertThat(deletedCar).isEmpty();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testViewAvailableCars_Success() throws Exception {
        mockMvc.perform(get("/api/cars/available")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
    
    //BOOKING MODULE TEST
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCreateBooking_Success() throws Exception {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setUserId(user.getId());     // user saved in @BeforeEach
        bookingRequest.setCarId(car.getId());       // car saved in @BeforeEach
        bookingRequest.setStartDate(LocalDate.now().toString());
        bookingRequest.setEndDate(LocalDate.now().plusDays(2).toString());

        mockMvc.perform(post("/api/bookings/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(100.0)); // 2 days * 50
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllBookings_AsAdmin() throws Exception {
        mockMvc.perform(get("/api/bookings/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // PAYMENT MODULE TESTS 
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testMakePayment_Success() throws Exception {
        // First, create and save a booking
        Booking booking = new Booking();
        booking.setCar(car);
        booking.setUser(user);
        booking.setStartDate(LocalDate.now());
        booking.setEndDate(LocalDate.now().plusDays(2));
        booking.setTotalAmount(100.0);
        booking = bookingRepository.save(booking);

        // Create PaymentRequest object
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setBookingId(booking.getId());
        paymentRequest.setAmount(100.0);
        paymentRequest.setPaymentMethod("Credit Card");

        // Perform POST request with JSON body
        mockMvc.perform(post("/api/payments/make")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentStatus").value("SUCCESS"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllPayments_AsAdmin() throws Exception {
        mockMvc.perform(get("/api/payments/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}

