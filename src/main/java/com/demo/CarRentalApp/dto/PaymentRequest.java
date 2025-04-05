package com.demo.CarRentalApp.dto;
//used to receive payment request details
public class PaymentRequest {
	 private Long bookingId;  // ID of the booking for which the payment is made
	 private double amount;   // Amount that the user is paying
	 private String paymentMethod;  // Payment method chosen by the user
	 
	    // Getters and Setters
	public Long getBookingId() {
		return bookingId;
	}
	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	 

}
