package com.example.payment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentProcessorTest {

    @Mock
    DatabaseConnection databaseConnection;

    @Mock
    EmailService emailService;

    @Mock
    PaymentApi paymentApi;

    @InjectMocks
    PaymentProcessor paymentProcessor;





    /**
     * Verifies that paymentProcessor processes payment successfull.
     *
     * Tested scenarios:
     * - External payment service returns successful response
     * - Payment is persisted in database
     * - Confirmation email is sent to user
     * - processPayment returns true
     */
    @Test
    @DisplayName("Should process payment successfully")
    void shouldProcessPaymentSuccessfully() {
        PaymentApiResponse apiResponse = new PaymentApiResponse(true);

        when(paymentApi.charge(anyString(), anyDouble())).thenReturn(apiResponse);


        boolean result = paymentProcessor.processPayment(2000.00);

        verify(databaseConnection)
                .savePayment(2000.00, "SUCCESS");

        verify(emailService).sendPaymentConfirmation(anyString(), anyDouble());

        assertThat(result).isTrue();



    }

    /**
     * Verifies that PaymentProcessor handles failed payments correctly.
     *
     * Tested scenarios:
     * - External payment service returns failure response
     * - Payment is NOT persisted in database
     * - Confirmation email is NOT sent
     * - processPayment returns false
     */

    @Test
    @DisplayName("Should not save payment or send email when payment fails")
    void shouldProcessPaymentFailure() {
        PaymentApiResponse apiResponse = new PaymentApiResponse(false);

        when(paymentApi.charge(anyString(), anyDouble())).thenReturn(apiResponse);

        boolean result = paymentProcessor.processPayment(2000.00);

        verify(databaseConnection, never())
        .savePayment(anyDouble(), anyString());

        verify(emailService, never()).sendPaymentConfirmation(anyString(), anyDouble());

        assertThat(result).isFalse();

    }
}