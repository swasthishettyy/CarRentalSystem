package com.demo.CarRentalApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.CarRentalApp.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Get all payments made by a specific user
    List<Payment> findByUserId(Long userId);
}