package com.example.payment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
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




    @Test
    void processPayment() {

    }
}