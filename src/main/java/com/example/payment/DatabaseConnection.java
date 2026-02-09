package com.example.payment;

public interface DatabaseConnection {

    void savePayment(double amount, String status);

}
