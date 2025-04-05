package com.demo.CarRentalApp.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.CarRentalApp.entity.Car;
import com.demo.CarRentalApp.repository.CarRepository;

@Service
public class CarService {
	private static final Logger logger = LoggerFactory.getLogger(CarService.class);

    @Autowired
    private CarRepository carRepository;

    // Add a car (Admin only)
    public Car addCar(Car car) {
    	car.setStatus("available"); // Default
    	Car savedCar = carRepository.save(car);
        logger.info("Car added successfully: {} - {}", car.getCarBrand(), car.getCarModel());
        return savedCar;
    }

    // Update car details by ID (Admin only)
    public Car updateCar(Long id, Car updatedCar) {
    	logger.info("Updating car with ID: {}", id);
        Optional<Car> optionalCar = carRepository.findById(id);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
            car.setCarModel(updatedCar.getCarModel());
            car.setCarBrand(updatedCar.getCarBrand());
            car.setPricePerDay(updatedCar.getPricePerDay());
            car.setStatus(updatedCar.getStatus());
            Car savedCar = carRepository.save(car);
            logger.info("Car updated successfully: {} - {}", car.getCarBrand(), car.getCarModel());
            return savedCar;
        }
        logger.warn("Car update failed: Car not found with ID {}", id);
        throw new RuntimeException("Car not found with id " + id);
    }

    // Delete a car by ID (Admin only)
    public void deleteCar(Long id) {
    	logger.info("Deleting car with ID: {}", id);
    	if (!carRepository.existsById(id)) {
            logger.warn("Car deletion failed: Car not found with ID {}", id);
            throw new RuntimeException("Car not found with id " + id);
        }

        carRepository.deleteById(id);
        logger.info("Car deleted successfully with ID: {}", id);
    }

    // Get all cars (Admin)
    public List<Car> getAllCars() {
    	logger.info("Fetching all cars");
        return carRepository.findAll();
    }

    // Get only available cars (User)
    public List<Car> getAvailableCars() {
    	logger.info("Fetching available cars");
        return carRepository.findByStatus("available");
    }

    // Get car by ID (for both Admin/User)
    public Car getCarById(Long id) {
    	logger.info("Fetching car details for ID: {}", id);
    	return carRepository.findById(id).orElseThrow(() -> {
            logger.error("Car not found with ID {}", id);
            return new RuntimeException("Car not found");
        });
    }
}