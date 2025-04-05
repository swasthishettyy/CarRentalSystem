package com.demo.CarRentalApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.CarRentalApp.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // Finds a user by username ( used  to authenticate a user)
    User findByUsername(String username);
}