package com.demo.CarRentalApp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.CarRentalApp.entity.Car;
import com.demo.CarRentalApp.service.CarService;

@RestController
@RequestMapping("/api/cars")
public class CarController {
	private static final Logger logger = LoggerFactory.getLogger(CarController.class);


    @Autowired
    private CarService carService;

    // Admin: Add a car
    @PostMapping("/add")
    public Car addCar(@RequestBody Car car) {
    	logger.info("Adding a new car: {}", car.getCarModel());
        Car addedCar = carService.addCar(car);
        logger.info("Car added successfully: ID = {}, Model = {}", addedCar.getId(), addedCar.getCarModel());
        return addedCar;
    }

    // Admin: Update a car
    @PutMapping("/update/{id}")
    public Car updateCar(@PathVariable Long id, @RequestBody Car car) {
    	logger.info("Updating car with ID: {}", id);
        Car updatedCar = carService.updateCar(id, car);
        logger.info("Car updated successfully: ID = {}, Model = {}", updatedCar.getId(), updatedCar.getCarModel());
        return updatedCar;
    }

    // Admin: Delete a car
    @DeleteMapping("/delete/{id}")
    public String deleteCar(@PathVariable Long id) {
    	logger.info("Deleting car with ID: {}", id);
        carService.deleteCar(id);
        logger.info("Car deleted successfully: ID = {}", id);
        return "Car deleted successfully!";
    }

    // Admin: Get all cars
    @GetMapping("/all")
    public List<Car> getAllCars() {
    	logger.info("Fetching all cars");
        List<Car> cars = carService.getAllCars();
        logger.info("Total cars fetched: {}", cars.size());
        return cars;
    }

    // User: Get only available cars
    @GetMapping("/available")
    public List<Car> getAvailableCars() {
    	logger.info("Fetching available cars...");
        List<Car> availableCars = carService.getAvailableCars();
        logger.info("Total available cars: {}", availableCars.size());
        return availableCars;
    }

    // Get car by ID
    @GetMapping("/{id}")
    public Car getCarById(@PathVariable Long id) {
    	logger.info("Fetching car details for ID: {}", id);
        Car car = carService.getCarById(id);
        logger.info("Car found: ID = {}, Model = {}", car.getId(), car.getCarModel());
        return car;
    }
}