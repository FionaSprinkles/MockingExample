package com.example.payment;

public class PaymentProcessor {

    private final PaymentApi paymentApi;
    private final DatabaseConnection databaseConnection;
    private EmailService emailService;

    private static final String API_KEY = "sk_test_123456";

    public PaymentProcessor(DatabaseConnection databaseConnection, PaymentApi paymentApi, EmailService emailService) {
        this.databaseConnection = databaseConnection;
        this.paymentApi = paymentApi;
        this.emailService = emailService;
    }



    public boolean processPayment(double amount) {
        // Anropar extern betaltjänst direkt med statisk API-nyckel
        PaymentApiResponse response = paymentApi.charge(API_KEY, amount);

        // Skriver till databas direkt
        if (response.isSuccess()) {
            databaseConnection.savePayment(amount, "SUCCESS");
        }

        // Skickar e-post direkt
        if (response.isSuccess()) {
            emailService.sendPaymentConfirmation("user@example.com", amount);
        }

        return response.isSuccess();
    }
}
