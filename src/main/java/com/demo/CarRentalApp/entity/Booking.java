package com.demo.CarRentalApp.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
//Represents a booking made by a user for a specific car.
@Entity
public class Booking {
	@Id   // Primary key for the Booking entity
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne   // Many bookings can be made by one user (ManyToOne relationship)
    @JoinColumn(name = "user_id", nullable = false)  // Foreign key to User table
    private User user;

    @ManyToOne   // Many bookings can be made for one car (ManyToOne relationship)
    @JoinColumn(name = "car_id", nullable = false)  // Foreign key to Car table
    private Car car;

    private LocalDate startDate;  // Start date of the booking
    private LocalDate endDate;    // End date of the booking
    private double totalAmount;   // Total amount to be paid for the booking
    private String status = "Booked";   // Status of the booking, default is "Booked"
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Car getCar() {
		return car;
	}
	public void setCar(Car car) {
		this.car = car;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Booking( User user, Car car, LocalDate startDate, LocalDate endDate, double totalAmount) {
		super();
		
		this.user = user;
		this.car = car;
		this.startDate = startDate;
		this.endDate = endDate;
		this.totalAmount = totalAmount;
		this.status = "Booked";  // Default booking status
	}
	public Booking() {
		super();
		// TODO Auto-generated constructor stub
	}
    


}
