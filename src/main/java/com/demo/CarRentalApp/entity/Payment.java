package com.demo.CarRentalApp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
// Represents a payment made by a user for a specific booking.
@Entity
public class Payment {
	@Id   // Primary key for the Payment entity
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne  // Many payments can be associated with one user
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // User who made the payment

    @ManyToOne  // Each payment is tied to one booking
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking; // The booking associated with this payment

    private double amountPaid;    // Amount that was paid
    private String paymentStatus; // Status of the payment:"SUCCESS", "FAILED", "PENDING"
    private String paymentMethod;  // Method used for the payment: "Credit Card", "Debit Card", "UPI", "Net Banking"
    private LocalDateTime paymentDate;  // Timestamp when the payment was made
    
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
	public Booking getBooking() {
		return booking;
	}
	public void setBooking(Booking booking) {
		this.booking = booking;
	}
	public double getAmountPaid() {
		return amountPaid;
	}
	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}
	
	
	public Payment(User user, Booking booking, double amountPaid, String paymentStatus, String paymentMethod) {
		super();
		this.user = user;
		this.booking = booking;
		this.amountPaid = amountPaid;
		this.paymentStatus = paymentStatus;
		this.paymentMethod = paymentMethod;
		this.paymentDate = LocalDateTime.now();
	}
	public Payment() {
		super();
		// TODO Auto-generated constructor stub
	}
    

}
