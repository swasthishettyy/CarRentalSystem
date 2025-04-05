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

import com.demo.CarRentalApp.entity.Car;
import com.demo.CarRentalApp.repository.CarRepository;

@ExtendWith(MockitoExtension.class) // Enables JUnit 5 
class CarServiceTest {

    @Mock
    private CarRepository carRepository; // Mocked CarRepository

    @InjectMocks
    private CarService carService; // CarService with injected mocks

    private Car car;

    @BeforeEach
    void setUp() {
    	// Create a sample car for test setup
        car = new Car();
        car.setId(1L);
        car.setCarBrand("Toyota");
        car.setCarModel("Corolla");
        car.setPricePerDay(50.0);
        car.setStatus("available");
    }
    //Add a new car 
    @Test
    void testAddCar() {
        when(carRepository.save(any(Car.class))).thenReturn(car);
        Car savedCar = carService.addCar(new Car());
        assertNotNull(savedCar);
        assertEquals("Toyota", savedCar.getCarBrand());
        verify(carRepository, times(1)).save(any(Car.class));
    }
    //Update an existing car
    @Test
    void testUpdateCar_Success() {
        Car updatedCar = new Car();
        updatedCar.setCarBrand("Honda");
        updatedCar.setCarModel("Civic");
        updatedCar.setPricePerDay(60.0);
        updatedCar.setStatus("unavailable");

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carRepository.save(any(Car.class))).thenReturn(updatedCar);

        Car result = carService.updateCar(1L, updatedCar);

        assertNotNull(result);
        assertEquals("Honda", result.getCarBrand());
        assertEquals("Civic", result.getCarModel());
        assertEquals(60.0, result.getPricePerDay());
        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).save(any(Car.class));
    }
    //Update car fails when car ID not found.
    @Test
    void testUpdateCar_NotFound() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> carService.updateCar(1L, car));
        assertEquals("Car not found with id 1", exception.getMessage());
        verify(carRepository, times(1)).findById(1L);
    }
    //Delete car successfully if car exists.
    @Test
    void testDeleteCar_Success() {
        when(carRepository.existsById(1L)).thenReturn(true);
        doNothing().when(carRepository).deleteById(1L);

        assertDoesNotThrow(() -> carService.deleteCar(1L));
        verify(carRepository, times(1)).deleteById(1L);
    }
    //Delete car fails if car doesn't exist.
    @Test
    void testDeleteCar_NotFound() {
        when(carRepository.existsById(1L)).thenReturn(false);
        Exception exception = assertThrows(RuntimeException.class, () -> carService.deleteCar(1L));
        assertEquals("Car not found with id 1", exception.getMessage());
        verify(carRepository, times(1)).existsById(1L);
    }
    // Get all cars
    @Test
    void testGetAllCars() {
        List<Car> carList = Arrays.asList(car, new Car());
        when(carRepository.findAll()).thenReturn(carList);

        List<Car> result = carService.getAllCars();
        assertEquals(2, result.size());
        verify(carRepository, times(1)).findAll();
    }
    // Get only available cars.
    @Test
    void testGetAvailableCars() {
        when(carRepository.findByStatus("available")).thenReturn(Arrays.asList(car));

        List<Car> availableCars = carService.getAvailableCars();
        assertEquals(1, availableCars.size());
        assertEquals("available", availableCars.get(0).getStatus());
        verify(carRepository, times(1)).findByStatus("available");
    }
    //Fetch car by ID successfully
    @Test
    void testGetCarById_Success() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        Car foundCar = carService.getCarById(1L);
        assertNotNull(foundCar);
        assertEquals("Toyota", foundCar.getCarBrand());
        verify(carRepository, times(1)).findById(1L);
    }
    //Fetch car by ID fails if not found
    @Test
    void testGetCarById_NotFound() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> carService.getCarById(1L));
        assertEquals("Car not found", exception.getMessage());
        verify(carRepository, times(1)).findById(1L);
    }
}
