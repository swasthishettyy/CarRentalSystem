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


import com.demo.CarRentalApp.entity.Car;
import com.demo.CarRentalApp.service.CarService;

@ExtendWith(MockitoExtension.class) // Enables Mockito for unit testing
class CarControllerTest {
    
    @Mock // Mocking the CarService
    private CarService carService;

    @InjectMocks // Inject mock service into controller
    private CarController carController;

    private Car car;

    @BeforeEach
    void setUp() {
    	// Initialize a sample Car object before each test
        car = new Car();
        car.setId(1L);
        car.setCarModel("Tesla Model S");
    }
    //Unit test for adding a new car
    @Test
    void testAddCar() {
    	// Mocking service behavior
        when(carService.addCar(any(Car.class))).thenReturn(car);
        // Call controller method
        Car result = carController.addCar(car);
        // Assertions
        assertNotNull(result);
        assertEquals("Tesla Model S", result.getCarModel());
        verify(carService, times(1)).addCar(car);
    }
    //Unit test for updating an existing car
    @Test
    void testUpdateCar() {
    	// Mocking service behavior for update
        when(carService.updateCar(eq(1L), any(Car.class))).thenReturn(car);
        // Call controller method
        Car result = carController.updateCar(1L, car);
        // Assertions
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Tesla Model S", result.getCarModel());
        verify(carService, times(1)).updateCar(1L, car);
    }

    //Unit test for deleting a car by ID
    @Test
    void testDeleteCar() {
        doNothing().when(carService).deleteCar(1L);
     // Call controller method
        String response = carController.deleteCar(1L);
     // Assertions
        assertEquals("Car deleted successfully!", response);
        verify(carService, times(1)).deleteCar(1L);
    }

    //Unit test for fetching all cars
    @Test
    void testGetAllCars() {
        // Creating mock Car objects
        Car car1 = new Car();
        car1.setId(1L);
        car1.setCarModel("Tesla Model S");

        Car car2 = new Car();
        car2.setId(2L);
        car2.setCarModel("BMW X5");

        List<Car> mockCarList = Arrays.asList(car1, car2);

        // Mocking the service call
        when(carService.getAllCars()).thenReturn(mockCarList);

        //  Calling the controller method
        List<Car> result = carController.getAllCars();

        // Assertions
        assertNotNull(result, "The result should not be null");
        assertEquals(2, result.size(), "The list size should be 2");
        assertEquals("Tesla Model S", result.get(0).getCarModel(), "First car model should match");
        assertEquals("BMW X5", result.get(1).getCarModel(), "Second car model should match");

        // Verify that service method was called exactly once
        verify(carService, times(1)).getAllCars();
    }
    
    //Unit test for fetching available cars
    @Test
    void testGetAvailableCars() {
    	// Mock available cars
        List<Car> availableCars = Arrays.asList(car);
        when(carService.getAvailableCars()).thenReturn(availableCars);
        // Call controller method
        List<Car> result = carController.getAvailableCars();
        // Assertions
        assertEquals(1, result.size());
        assertEquals("Tesla Model S", result.get(0).getCarModel());
        verify(carService, times(1)).getAvailableCars();
    }
    //Unit test for fetching a car by its ID
    @Test
    void testGetCarById() {
    	// Mock service to return a car
        when(carService.getCarById(1L)).thenReturn(car);
        // Call controller method
        Car result = carController.getCarById(1L);
        // Assertions
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Tesla Model S", result.getCarModel());
        verify(carService, times(1)).getCarById(1L);
    }
}
