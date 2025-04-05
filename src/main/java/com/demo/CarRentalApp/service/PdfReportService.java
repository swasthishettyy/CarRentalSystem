package com.demo.CarRentalApp.service;

import com.demo.CarRentalApp.entity.Booking;
import com.demo.CarRentalApp.entity.Payment;
import com.demo.CarRentalApp.entity.User;
import com.demo.CarRentalApp.repository.BookingRepository;
import com.demo.CarRentalApp.repository.PaymentRepository;
import com.demo.CarRentalApp.repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfReportService {
	private static final Logger logger = LoggerFactory.getLogger(PdfReportService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public byte[] generateUserReport(String username) {
    	logger.info("Generating PDF report for user: {}", username);
    	// Find user by username
        User user = userRepository.findByUsername(username);
        if (user == null) {
        	logger.error("Report generation failed: User not found - {}", username);
            throw new RuntimeException("User not found!");
        }

        // Get user bookings
        List<Booking> bookings = bookingRepository.findByUserId(user.getId());
        if (bookings.isEmpty()) {
        	logger.warn("No bookings found for user: {}. Report cannot be generated.", username);
            throw new RuntimeException("No bookings found for this user. Report cannot be generated.");
        }

        // Get user payments
        List<Payment> payments = paymentRepository.findByUserId(user.getId());

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("User Booking & Payment Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // User Details
            document.add(new Paragraph("User Details:"));
            document.add(new Paragraph("Username: " + user.getUsername()));
            document.add(new Paragraph("Role: " + user.getRole()));
            document.add(new Paragraph("\n"));

            // Booking Details
            document.add(new Paragraph("Booking Details:"));
            for (Booking booking : bookings) {
                document.add(new Paragraph("Car ID: " + booking.getCar().getId() +
                        ", Start Date: " + booking.getStartDate() +
                        ", End Date: " + booking.getEndDate() +
                        ", Amount: " + booking.getTotalAmount()));
            }
            document.add(new Paragraph("\n"));

            // Payment Details
            document.add(new Paragraph("Payment Details:"));
            for (Payment payment : payments) {
                document.add(new Paragraph("Payment ID: " + payment.getId() +
                        ", Amount: " + payment.getAmountPaid() +
                        ", Status: " + payment.getPaymentStatus()));
            }

            document.close();
            logger.info("PDF report generated successfully for user: {}", username);
            return outputStream.toByteArray();
        } catch (Exception e) {
        	logger.error("Error generating PDF report for user: {}", username, e);
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
