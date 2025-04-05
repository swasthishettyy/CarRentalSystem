package com.demo.CarRentalApp.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
//Represents a user of the car rental system
@Entity
public class User  {
	@Id  // Primary key for the User entity
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;   // Username for login
    
    private String password;   // Password for authentication
    // Role of the user: either ROLE_ADMIN or ROLE_USER
    @Enumerated(EnumType.STRING)
    private Role role;


    // One user can have many bookings (mapped by "user" in the Booking entity)
    @OneToMany(mappedBy = "user")
    @JsonIgnore   // Prevents infinite recursion during JSON serialization
    private List<Booking> bookings;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public User( String username, String password, Role role) {
		super();
		
		this.username = username;
		this.password = password;
		this.role = role;
		
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
   //Enum to define user roles in the system.
   //ROLE_ADMIN - Full access (manage cars, bookings, payments)
   //ROLE_USER  - Limited access (view cars, create/view own bookings/payments)
	public enum Role {
	    ROLE_ADMIN, ROLE_USER
	}
    

}
