package com.demo.CarRentalApp.dto;
//used to receive booking request details 
public class BookingRequest {
	private Long userId;   // ID of the user making the booking
    private Long carId;     // ID of the car to be booked
    private String startDate; // Booking start date
    private String endDate;   // Booking end date 
    
    // Getters and Setters
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getCarId() {
		return carId;
	}
	public void setCarId(Long carId) {
		this.carId = carId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
    

}
