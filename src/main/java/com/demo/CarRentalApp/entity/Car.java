package com.demo.CarRentalApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Car {
	@Id  // Primary key for the Car entity
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String carModel;   // Model name of the car 
    private String carBrand;   // Brand name of the car 
    private double pricePerDay; // Price per day to rent the car
    @JsonIgnore 
    private String status = "available"; // default status
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCarModel() {
		return carModel;
	}
	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}
	public String getCarBrand() {
		return carBrand;
	}
	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}
	public double getPricePerDay() {
		return pricePerDay;
	}
	public void setPricePerDay(double pricePerDay) {
		this.pricePerDay = pricePerDay;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Car( String carModel, String carBrand, double pricePerDay, String status) {
		super();
	
		this.carModel = carModel;
		this.carBrand = carBrand;
		this.pricePerDay = pricePerDay;
		this.status = status;
	}
	public Car() {
		super();
		// TODO Auto-generated constructor stub
	}
    

}
