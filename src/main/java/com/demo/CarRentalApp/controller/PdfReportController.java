package com.demo.CarRentalApp.controller;

import com.demo.CarRentalApp.service.PdfReportService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/api/reports")
public class PdfReportController {
	private static final Logger logger = LoggerFactory.getLogger(PdfReportController.class);

    @Autowired
    private PdfReportService pdfreportService;

    // Download PDF report (Admin & User)
    @GetMapping("/download")
    public void downloadUserReport(Authentication authentication, HttpServletResponse response) {
        try {
            String username = authentication.getName();
            byte[] pdfReport = pdfreportService.generateUserReport(username);

            // Set response headers
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=User_Report.pdf");
            response.setContentLength(pdfReport.length);

            // Write PDF to response output stream
            OutputStream out = response.getOutputStream();
            out.write(pdfReport);
            out.flush();
            out.close();
            logger.info("Report successfully generated and sent to user: {}", username);
        } catch (RuntimeException | IOException e) {
        	
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
