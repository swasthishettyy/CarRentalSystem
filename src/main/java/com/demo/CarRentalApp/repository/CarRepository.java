package com.demo.CarRentalApp.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.CarRentalApp.entity.Car;
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByStatus(String status); // For users to get only "available" cars
}

