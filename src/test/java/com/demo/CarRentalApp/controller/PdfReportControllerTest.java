package com.demo.CarRentalApp.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

import com.demo.CarRentalApp.service.PdfReportService;

@ExtendWith(MockitoExtension.class)  // Enables Mockito for JUnit 5
class PdfReportControllerTest {

    @Mock
    private PdfReportService pdfReportService; // Mocked service that generates PDF reports

    @Mock
    private Authentication authentication;  // Mocked Authentication object to simulate user identity

    @Mock
    private HttpServletResponse response;  // Mocked response used in error scenario

    @InjectMocks
    private PdfReportController pdfReportController;  // Controller under test

    private byte[] mockPdfData;

    @BeforeEach
    void setUp() {
        // Prepare mock PDF content as byte array
        mockPdfData = "Mock PDF Data".getBytes();
    }

    @Test
    void testDownloadUserReport_Success() throws IOException {
        // authenticated user's username
        when(authentication.getName()).thenReturn("testuser");
        when(pdfReportService.generateUserReport("testuser")).thenReturn(mockPdfData);


        // Using Spring's MockHttpServletResponse to capture output and headers
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        // Call the controller method
        pdfReportController.downloadUserReport(authentication, mockResponse);

        // Assertion
        assertArrayEquals(mockPdfData, mockResponse.getContentAsByteArray(), "Generated PDF data should match");

        // Verify response headers
        assertEquals("application/pdf", mockResponse.getContentType());
        assertEquals("attachment; filename=User_Report.pdf", mockResponse.getHeader("Content-Disposition"));
        assertEquals(mockPdfData.length, mockResponse.getContentLength());

        // Verify service method was called
        verify(pdfReportService, times(1)).generateUserReport("testuser");
    }


    @Test
    void testDownloadUserReport_ExceptionHandling() throws IOException {
        //Simulate authenticated user
        when(authentication.getName()).thenReturn("testuser");
        when(pdfReportService.generateUserReport("testuser")).thenThrow(new RuntimeException("Report generation failed"));

        // Call the controller method with mocked HttpServletResponse
        pdfReportController.downloadUserReport(authentication, response);

        // Verify that the error response is sent
        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Report generation failed");

        // Verify that the service method was called once
        verify(pdfReportService, times(1)).generateUserReport("testuser");
    }
}
